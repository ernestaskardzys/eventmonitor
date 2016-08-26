package info.ernestas.eventmonitor;

import info.ernestas.eventmonitor.dao.entity.Event;
import info.ernestas.eventmonitor.dao.entity.Version;
import info.ernestas.eventmonitor.model.dto.EventDto;

public class AbstractTest {

    public Version getVersion() {
        Version version = new Version();
        version.setVersion("1.0.0");
        version.setName("TestApplication");
        version.setAuthor("Ernestas Kardzys");

        return version;
    }

    public Event getEvent() {
        Event event = new Event();
        event.setId(1);
        event.setName("1 event");
        event.setTimestamp("16:21");
        event.setUpdatedBy(1);

        return event;
    }

    public EventDto getEventDto() {
        return new EventDto(1, "1 event", "16:21", 1);
    }

}
