package app.music.resourceprocessorservice.event.resourse;

import app.music.resourceprocessorservice.client.resource.ResourceServiceClient;
import app.music.resourceprocessorservice.client.song.SongServiceClient;
import app.music.resourceprocessorservice.common.RecordId;
import app.music.resourceprocessorservice.event.EventListener;
import app.music.resourceprocessorservice.processor.ResourceProcessor;
import app.music.resourceprocessorservice.processor.mp3.SongMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ResourceListener implements EventListener<ResourceEvent> {
    private final ResourceServiceClient resourceServiceClient;
    private final SongServiceClient songServiceClient;
    private final ResourceProcessor<SongMetadata> resourceProcessor;

    @Override
    public void handleEvent(ResourceEvent data) {
        RecordId res = resourceServiceClient.downloadFile(data.resourceId())
                .map(resourceProcessor::process)
                .map(Optional::orElseThrow)
                .map(songServiceClient::createSong)
                .orElse(null);

        log.info("song created {}", res);
    }
}
