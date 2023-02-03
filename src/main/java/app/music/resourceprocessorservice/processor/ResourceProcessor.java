package app.music.resourceprocessorservice.processor;

import java.io.File;
import java.util.Optional;

public interface ResourceProcessor<R> {

    Optional<R> process(File file);
}
