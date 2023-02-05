package app.music.resourceprocessorservice;

import app.music.resourceprocessorservice.event.resourse.ResourceEvent;
import app.music.resourceprocessorservice.event.resourse.ResourceListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class ResourceProcessorServiceApplication {

    private final ResourceListener resourceListener;

    public static void main(String[] args) {
        SpringApplication.run(ResourceProcessorServiceApplication.class, args);
    }


    @Scheduled(fixedDelay = 10000)
    public void test() {
        System.out.println("etre");
        resourceListener.handleEvent(new ResourceEvent(9L));
    }


}
