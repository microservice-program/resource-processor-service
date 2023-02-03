package app.music.resourceprocessorservice.client.song;

import app.music.resourceprocessorservice.common.RecordId;
import app.music.resourceprocessorservice.processor.mp3.SongMetadata;

import java.util.Optional;


public interface SongServiceClient {

    RecordId createSong(SongMetadata song);
}
