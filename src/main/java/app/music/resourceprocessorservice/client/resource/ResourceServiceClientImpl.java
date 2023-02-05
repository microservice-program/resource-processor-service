package app.music.resourceprocessorservice.client.resource;

import app.music.resourceprocessorservice.client.BaseWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
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
public class ResourceServiceClientImpl extends BaseWebClient implements ResourceServiceClient {
    private static final String URI = "localhost:8080/api/v1/resources";
    private final WebClient webClient;

    @Override
    public Optional<File> downloadFile(Long resourceId) {
        Flux<DataBuffer> res = webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path(URI)
                                .path("/{id}")
                                .build(resourceId))
                .accept(MediaType.ALL)
                .retrieve()
                .bodyToFlux(DataBuffer.class);
        return Optional.ofNullable(fluxToFile(res));
    }

    public File fluxToFile(Flux<DataBuffer> input) {
        Path path = Paths.get("temp");
        Mono<File> file = DataBufferUtils.write(input, path, StandardOpenOption.CREATE)
                .thenReturn(path.toFile());
        return file.block();
    }
}
