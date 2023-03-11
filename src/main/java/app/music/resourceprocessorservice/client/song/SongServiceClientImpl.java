package app.music.resourceprocessorservice.client.song;

import app.music.resourceprocessorservice.client.BaseWebClient;
import app.music.resourceprocessorservice.common.RecordId;
import app.music.resourceprocessorservice.processor.mp3.SongMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Log4j2
public class SongServiceClientImpl extends BaseWebClient implements SongServiceClient {
    private static final String URI = "localhost:8001/api/v1/songs";
    private final WebClient webClient;
    private final RetryTemplate retryTemplate;

    @Override
    public RecordId createSong(SongMetadata song) {
       return retryTemplate.execute(
                retry -> webClient.post()
                        .uri(uriBuilder -> uriBuilder.path(URI).build())
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(song)
                        .retrieve()
                        .bodyToMono(RecordId.class)
                        .block(),
                recover -> {
                    log.error("Error occurs during creating song: attempt = {}, resourceId = {}, error ={}",
                            recover.getRetryCount(),
                            song.resourceId(),
                            recover.getLastThrowable());
                    return null;
                });
    }
}
