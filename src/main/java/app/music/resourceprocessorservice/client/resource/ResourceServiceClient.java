package app.music.resourceprocessorservice.client.resource;

import java.io.File;
import java.util.Optional;

public interface ResourceServiceClient {

    Optional<File> downloadFile(Long resourceId);

}
