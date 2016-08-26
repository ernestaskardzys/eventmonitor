package info.ernestas.eventmonitor.service.activemq;

import info.ernestas.eventmonitor.AbstractTest;
import info.ernestas.eventmonitor.model.Action;
import info.ernestas.eventmonitor.model.EventStatusChange;
import info.ernestas.eventmonitor.model.JsonResult;
import info.ernestas.eventmonitor.model.dto.EventDto;
import info.ernestas.eventmonitor.model.dto.WebSocketDto;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ProcessedEventListenerTest extends AbstractTest {

    private static final String SERVER_LIST = "/server/list";

    private ProcessedEventListener processedEventListener;

    private SimpMessagingTemplate simpMessagingTemplate;

    @Before
    public void setUp() {
        simpMessagingTemplate = mock(SimpMessagingTemplate.class);

        processedEventListener = new ProcessedEventListener(simpMessagingTemplate);
    }

    @Test
    public void testOnMessage() throws JMSException {
        EventStatusChange eventStatusChange = new EventStatusChange(getEventDto(), Action.INSERT);
        JsonResult<WebSocketDto> jsonMessage = new JsonResult<>(new WebSocketDto(eventStatusChange.getAction().toString() + eventStatusChange.getEventDto()));

        ObjectMessage message = new ActiveMQObjectMessage();
        message.setObject(eventStatusChange);

        processedEventListener.onMessage(message);

        verify(simpMessagingTemplate, times(1)).convertAndSend(SERVER_LIST, jsonMessage);
    }

    @Test
    public void testOnMessage_withWrongTypeOfObject() throws JMSException {
        final EventDto eventDto = getEventDto();
        ObjectMessage message = new ActiveMQObjectMessage();
        message.setObject(eventDto);

        processedEventListener.onMessage(message);

        verify(simpMessagingTemplate, never()).convertAndSend(SERVER_LIST, eventDto);
    }

}