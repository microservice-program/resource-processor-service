package app.music.resourceprocessorservice.event;

import org.springframework.kafka.support.Acknowledgment;

public interface EventListener<T> {

    void handleEvent(T data, Acknowledgment acknowledgment);
}
