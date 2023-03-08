package app.music.resourceprocessorservice.event.resourse;

import app.music.resourceprocessorservice.client.resource.ResourceServiceClient;
import app.music.resourceprocessorservice.client.song.SongServiceClient;
import app.music.resourceprocessorservice.common.RecordId;
import app.music.resourceprocessorservice.event.EventListener;
import app.music.resourceprocessorservice.processor.ResourceProcessor;
import app.music.resourceprocessorservice.processor.mp3.SongMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ResourceListener implements EventListener<ResourceEvent> {
    private final ResourceServiceClient resourceServiceClient;
    private final SongServiceClient songServiceClient;
    private final ResourceProcessor<SongMetadata> resourceProcessor;

    @KafkaListener(topics = "resources", containerFactory = "kafkaListenerContainerFactory")
    @Override
    public void handleEvent(@Payload ResourceEvent data, Acknowledgment acknowledgment) {
        log.debug("Resource event received: {}", data);
        Long resourceId = data.resourceId();
        RecordId res = resourceServiceClient.downloadFile(resourceId)
                .map(resourceProcessor::process)
                .map(Optional::orElseThrow)
                .map(song ->
                        songServiceClient.createSong(song.setResourceId(resourceId)))
                .orElse(null);
        if (Objects.nonNull(res)) {
            acknowledgment.acknowledge();
        }
        log.info("song created {}", res);
    }

}
