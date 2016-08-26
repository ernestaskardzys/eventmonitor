package info.ernestas.eventmonitor.model;

import info.ernestas.eventmonitor.model.dto.EventDto;

import java.io.Serializable;

public class EventStatusChange implements Serializable {

    private EventDto eventDto;

    private Action action;

    public EventStatusChange(EventDto eventDto, Action action) {
        this.eventDto = eventDto;
        this.action = action;
    }

    public EventDto getEventDto() {
        return eventDto;
    }

    public Action getAction() {
        return action;
    }

}
