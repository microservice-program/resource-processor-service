package app.music.resourceprocessorservice;

import app.music.resourceprocessorservice.event.resourse.ResourceEvent;
import app.music.resourceprocessorservice.event.resourse.ResourceListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequiredArgsConstructor
public class ResourceProcessorServiceApplication {

    private final ResourceListener resourceListener;

    public static void main(String[] args) {
        SpringApplication.run(ResourceProcessorServiceApplication.class, args);
    }

    @GetMapping("/api/test/{id}")
    public void test(@PathVariable Long id) {
        resourceListener.handleEvent(new ResourceEvent(id));
    }


}
