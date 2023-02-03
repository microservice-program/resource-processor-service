package app.music.resourceprocessorservice.processor.mp3;

import app.music.resourceprocessorservice.exceptions.exception.BadRequestException;
import app.music.resourceprocessorservice.exceptions.exception.UnexpectedException;
import app.music.resourceprocessorservice.processor.ResourceProcessor;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Log4j2
@Service
public class Mp3Processor implements ResourceProcessor<SongMetadata> {
    @Override
    public Optional<SongMetadata> process(File file) {
        try {
            Mp3File mp3File = new Mp3File(file);
            return extractMetadata(mp3File);
        } catch (IOException e) {
            log.error(e);
            throw new UnexpectedException(e.getMessage());
        } catch (InvalidDataException | UnsupportedTagException e) {
            log.error(e);
            throw new BadRequestException(e.getMessage());
        }
    }

    private static Optional<SongMetadata> extractMetadata(Mp3File mp3File) {
        String duration = String.format("%1d:%2d",
                mp3File.getLengthInSeconds() / 60,
                mp3File.getLengthInSeconds() % 60);

        if (mp3File.hasId3v1Tag())
            return Optional.of(new SongMetadata(mp3File.getId3v1Tag().getTitle(),
                    mp3File.getId3v1Tag().getArtist(),
                    mp3File.getId3v1Tag().getAlbum(),
                    duration,
                    mp3File.getId3v1Tag().getYear()));


        if (mp3File.hasId3v2Tag())
            return Optional.of(new SongMetadata(mp3File.getId3v2Tag().getTitle(),
                    mp3File.getId3v2Tag().getArtist(),
                    mp3File.getId3v2Tag().getAlbum(),
                    duration,
                    mp3File.getId3v2Tag().getYear()));


        return Optional.of(
                new SongMetadata(
                        "No name: " + LocalDateTime.now(),
                        "No album",
                        "No Artist",
                        duration,
                        "")
        );
    }
}
