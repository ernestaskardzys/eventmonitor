package info.ernestas.eventmonitor.service;

import info.ernestas.eventmonitor.AbstractTest;
import info.ernestas.eventmonitor.dao.EventDao;
import info.ernestas.eventmonitor.dao.entity.Event;
import info.ernestas.eventmonitor.model.Action;
import info.ernestas.eventmonitor.model.dto.EventDto;
import info.ernestas.eventmonitor.service.mapper.EventEventDtoMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EventServiceImplTest extends AbstractTest {

    private static final int ONE_SECOND = 1000;

    private EventDao eventDao;

    private EventDto eventDto;

    private EventEventDtoMapper eventEventDtoMapper;

    private EventServiceImpl eventService;

    private JmsTemplate jmsTemplate;

    private String incomingEventQueue = "incomingEventQueue";

    private String processedEventQueue = "processedEventQueue";

    @Before
    public void setUp() {
        eventDto = getEventDto();

        eventDao = mock(EventDao.class);

        eventEventDtoMapper = mock(EventEventDtoMapper.class);

        jmsTemplate = mock(JmsTemplate.class);

        when(eventEventDtoMapper.convertDtoToEntity(eventDto)).thenReturn(getEvent());
        when(eventEventDtoMapper.convertEntityToDto(any(Event.class))).thenReturn(getEventDto());
        when(eventDao.save(any(Event.class))).thenReturn(getEvent());

        eventService = new EventServiceImpl(eventDao, eventEventDtoMapper, jmsTemplate);
        eventService.setIncomingEventQueue(incomingEventQueue);
        eventService.setProcessedEventQueue(processedEventQueue);
    }

    @Test
    public void testSaveIncomingEvent() {
        eventService.saveIncomingEvent(eventDto);

        verify(jmsTemplate, times(1)).send(eq(incomingEventQueue), any(MessageCreator.class));
    }

    @Test
    public void testProcessEvent_insertNewEntry() {
        EventDto result = eventService.processEvent(eventDto, Action.INSERT);

        verify(eventEventDtoMapper, times(1)).convertDtoToEntity(any(EventDto.class));
        verify(eventDao, times(1)).save(any(Event.class));
        verify(jmsTemplate, times(1)).send(eq(processedEventQueue), any(MessageCreator.class));
        verify(eventEventDtoMapper, times(2)).convertEntityToDto(any(Event.class));

        EventDto original = getEventDto();
        assertEquals(original.getId(), result.getId());
        assertEquals(original.getName(), result.getName());
        assertEquals(original.getTimestamp(), result.getTimestamp());
        assertEquals(original.getUpdatedBy(), result.getUpdatedBy());
    }

    @Test
    public void testProcessEvent_updateEntry_withoutDelay() {
        eventService.setEnableDelay(false);

        EventDto result = eventService.processEvent(eventDto, Action.UPDATE);

        verify(eventEventDtoMapper, times(1)).convertDtoToEntity(any(EventDto.class));
        verify(eventDao, times(1)).save(any(Event.class));
        verify(jmsTemplate, times(1)).send(eq(processedEventQueue), any(MessageCreator.class));
        verify(eventEventDtoMapper, times(1)).convertEntityToDto(any(Event.class));

        EventDto original = getEventDto();
        assertEquals(original.getId(), result.getId());
        assertEquals(original.getName(), result.getName());
        assertEquals(original.getTimestamp(), result.getTimestamp());
        assertEquals(original.getUpdatedBy(), result.getUpdatedBy());
    }

    // Since we use Thread.sleep() with very small delay - the test shouldn't take more than 1 s to complete
    @Test(timeout = ONE_SECOND)
    public void testProcessEvent_updateEntry_withDelay() {
        eventService.setEnableDelay(true);
        eventService.setEventProcessingInitialTime(1);
        eventService.setEventProcessingMaxRandomTime(1);

        EventDto result = eventService.processEvent(eventDto, Action.UPDATE);

        verify(eventEventDtoMapper, times(1)).convertDtoToEntity(any(EventDto.class));
        verify(eventDao, times(1)).save(any(Event.class));
        verify(jmsTemplate, times(1)).send(eq(processedEventQueue), any(MessageCreator.class));
        verify(eventEventDtoMapper, times(1)).convertEntityToDto(any(Event.class));

        EventDto original = getEventDto();
        assertEquals(original.getId(), result.getId());
        assertEquals(original.getName(), result.getName());
        assertEquals(original.getTimestamp(), result.getTimestamp());
        assertEquals(original.getUpdatedBy(), result.getUpdatedBy());
    }

    @Test
    public void testProcessEvent_delete() {
        EventDto result = eventService.processEvent(eventDto, Action.DELETE);

        verify(eventEventDtoMapper, times(1)).convertDtoToEntity(any(EventDto.class));
        verify(eventDao, times(1)).delete(any(Event.class));
        verify(jmsTemplate, times(1)).send(eq(processedEventQueue), any(MessageCreator.class));
        verify(eventEventDtoMapper, times(1)).convertEntityToDto(any(Event.class));

        EventDto original = getEventDto();
        assertEquals(original.getId(), result.getId());
        assertEquals(original.getName(), result.getName());
        assertEquals(original.getTimestamp(), result.getTimestamp());
        assertEquals(original.getUpdatedBy(), result.getUpdatedBy());
    }

    @Test
    public void testProcessEvent_Error() {
        EventDto result = eventService.processEvent(eventDto, Action.ERROR);

        verify(eventEventDtoMapper, times(1)).convertDtoToEntity(any(EventDto.class));
        verify(jmsTemplate, times(1)).send(eq(processedEventQueue), any(MessageCreator.class));
        verify(eventEventDtoMapper, times(1)).convertEntityToDto(any(Event.class));

        EventDto original = getEventDto();
        assertEquals(original.getId(), result.getId());
        assertEquals(original.getName(), result.getName());
        assertEquals(original.getTimestamp(), result.getTimestamp());
        assertEquals(original.getUpdatedBy(), result.getUpdatedBy());
    }

}