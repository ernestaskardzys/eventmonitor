package info.ernestas.eventmonitor.service.mapper;

import info.ernestas.eventmonitor.dao.entity.Event;
import info.ernestas.eventmonitor.model.dto.EventDto;
import org.springframework.stereotype.Component;

@Component
public class EventEventDtoMapper implements EntityDtoMapper<EventDto, Event> {

    @Override
    public EventDto convertEntityToDto(Event entity) {
        if (entity == null) {
            return new EventDto();
        }

        return new EventDto(entity.getId(), entity.getName(), entity.getTimestamp(), entity.getUpdatedBy());
    }

    @Override
    public Event convertDtoToEntity(EventDto dto) {
        Event event = new Event();
        event.setId(dto.getId());
        event.setName(dto.getName());
        event.setTimestamp(dto.getTimestamp());
        event.setUpdatedBy(dto.getUpdatedBy());
        return event;
    }

}
