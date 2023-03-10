package app.music.resourceprocessorservice.client.resource;

import app.music.resourceprocessorservice.client.BaseWebClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ResourceServiceClientImpl extends BaseWebClient implements ResourceServiceClient {
    private static final String URI = "localhost:8000/api/v1/resources";
    private final WebClient webClient;
    private final RetryTemplate retryTemplate;

    @Override
    public Optional<File> downloadFile(Long resourceId) {
        return retryTemplate.execute(retry -> {
            Flux<DataBuffer> res = webClient.get()
                    .uri(uriBuilder ->
                            uriBuilder.path(URI)
                                    .path("/{id}")
                                    .build(resourceId))
                    .accept(MediaType.ALL)
                    .retrieve()
                    .bodyToFlux(DataBuffer.class);
            return Optional.of(fluxToFile(res));
        }, recover -> {
            log.error("Error occurs during creating song: attempt = {}, resourceId = {}, error ={}",
                    recover.getRetryCount(),
                    resourceId,
                    recover.getLastThrowable());
            return Optional.empty();
        });

    }

    public File fluxToFile(Flux<DataBuffer> input) {
        Path path = Paths.get("temp");
        Mono<File> file = DataBufferUtils.write(input, path, StandardOpenOption.CREATE)
                .thenReturn(path.toFile());
        return file.block();
    }
}
