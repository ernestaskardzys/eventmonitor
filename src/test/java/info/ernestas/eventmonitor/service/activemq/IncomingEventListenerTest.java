package info.ernestas.eventmonitor.service.activemq;

import info.ernestas.eventmonitor.AbstractTest;
import info.ernestas.eventmonitor.model.Action;
import info.ernestas.eventmonitor.model.dto.EventDto;
import info.ernestas.eventmonitor.service.EventService;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.junit.Before;
import org.junit.Test;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IncomingEventListenerTest extends AbstractTest {

    private EventDto eventDto;

    private EventService eventService;

    private IncomingEventListener incomingEventListener;

    @Before
    public void setUp() {
        eventDto = getEventDto();

        eventService = mock(EventService.class);

        when(eventService.processEvent(eventDto, Action.INSERT)).thenReturn(eventDto);
        when(eventService.processEvent(eventDto, Action.UPDATE)).thenReturn(eventDto);
        when(eventService.processEvent(eventDto, Action.DELETE)).thenReturn(eventDto);

        incomingEventListener = new IncomingEventListener(eventService);
    }

    @Test
    public void testOnMessage() throws JMSException {
        EventDto eventDto = getEventDto();

        ObjectMessage message = new ActiveMQObjectMessage();
        message.setObject(eventDto);

        incomingEventListener.onMessage(message);

        verify(eventService, times(1)).processEvent(eventDto, Action.INSERT);
        verify(eventService, times(1)).processEvent(eventDto, Action.UPDATE);
        verify(eventService, times(1)).processEvent(eventDto, Action.DELETE);
    }

    @Test
    public void testOnMessage_withWrongTypeOfObject() throws JMSException {
        ObjectMessage message = new ActiveMQObjectMessage();
        message.setObject(getEvent());

        incomingEventListener.onMessage(message);

        verify(eventService, never()).processEvent(any(), any());
    }

}