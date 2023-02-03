package app.music.resourceprocessorservice.processor.mp3;

public record SongMetadata(String resourceId,
                           String name,
                           String album,
                           String artist,
                           String length,
                           String year
) {


    public SongMetadata(String name, String album, String artist, String length, String year) {
        this(null, name, album, artist, length, year);
    }
}
