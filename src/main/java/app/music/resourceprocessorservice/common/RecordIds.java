package app.music.resourceprocessorservice.common;

import java.util.List;

public record RecordIds(List<Long> ids) {
    public void addElement(Long id) {
        ids.add(id);
    }
}
