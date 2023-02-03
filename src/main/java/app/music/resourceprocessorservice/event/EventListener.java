package app.music.resourceprocessorservice.event;

public interface EventListener<T> {

    void handleEvent(T data);
}
