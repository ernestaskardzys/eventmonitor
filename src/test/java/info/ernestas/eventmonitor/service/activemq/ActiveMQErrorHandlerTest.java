package info.ernestas.eventmonitor.service.activemq;

import info.ernestas.eventmonitor.model.Action;
import info.ernestas.eventmonitor.model.JsonResult;
import info.ernestas.eventmonitor.model.dto.WebSocketDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ActiveMQErrorHandlerTest {

    private ActiveMQErrorHandler activeMQErrorHandler;

    private SimpMessagingTemplate simpMessagingTemplate;

    @Before
    public void setUp() {
        simpMessagingTemplate = mock(SimpMessagingTemplate.class);

        activeMQErrorHandler = new ActiveMQErrorHandler(simpMessagingTemplate);
    }

    @Test
    public void testHandleError() {
        Throwable exception = new Exception("Error");
        JsonResult<WebSocketDto> jsonMessage = new JsonResult<>(new WebSocketDto(Action.ERROR.toString() + " " + exception.toString()));

        activeMQErrorHandler.handleError(exception);

        verify(simpMessagingTemplate, times(1)).convertAndSend("/server/list", jsonMessage);
    }

}