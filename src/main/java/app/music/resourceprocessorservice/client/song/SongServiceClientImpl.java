package app.music.resourceprocessorservice.client.song;

import app.music.resourceprocessorservice.client.BaseWebClient;
import app.music.resourceprocessorservice.common.RecordId;
import app.music.resourceprocessorservice.processor.mp3.SongMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class SongServiceClientImpl extends BaseWebClient implements SongServiceClient {
    private static final String URI = "localhost:8001/api/v1/songs";
    private final WebClient webClient;

    @Override
    public RecordId createSong(SongMetadata song) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(URI).build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(song)
                .retrieve()
                .bodyToMono(RecordId.class)
                .block();
    }
}
