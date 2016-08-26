package info.ernestas.eventmonitor.service;

import info.ernestas.eventmonitor.model.Action;
import info.ernestas.eventmonitor.model.dto.EventDto;

public interface EventService {

    void saveIncomingEvent(EventDto eventDto);

    EventDto processEvent(EventDto event, Action action);

}
