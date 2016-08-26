package info.ernestas.eventmonitor.service.mapper;

import info.ernestas.eventmonitor.AbstractTest;
import info.ernestas.eventmonitor.dao.entity.Event;
import info.ernestas.eventmonitor.model.dto.EventDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventEventDtoMapperTest extends AbstractTest {

    private EventEventDtoMapper eventEventDtoMapper;

    @Before
    public void setUp() {
        eventEventDtoMapper = new EventEventDtoMapper();
    }

    @Test
    public void testConvertEntityToDto() {
        Event event = getEvent();

        EventDto eventDto = eventEventDtoMapper.convertEntityToDto(event);

        assertEquals(event.getId(), eventDto.getId());
        assertEquals(event.getName(), eventDto.getName());
        assertEquals(event.getTimestamp(), eventDto.getTimestamp());
        assertEquals(event.getUpdatedBy(), eventDto.getUpdatedBy());
    }

    @Test
    public void testConvertDtoToEntity() {
        EventDto eventDto = getEventDto();

        Event event = eventEventDtoMapper.convertDtoToEntity(eventDto);

        assertEquals(eventDto.getId(), event.getId());
        assertEquals(eventDto.getName(), event.getName());
        assertEquals(eventDto.getTimestamp(), event.getTimestamp());
        assertEquals(eventDto.getUpdatedBy(), event.getUpdatedBy());
    }

}