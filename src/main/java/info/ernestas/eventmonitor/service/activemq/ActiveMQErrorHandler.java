package info.ernestas.eventmonitor.service.activemq;

import info.ernestas.eventmonitor.model.Action;
import info.ernestas.eventmonitor.model.JsonResult;
import info.ernestas.eventmonitor.model.dto.WebSocketDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
public class ActiveMQErrorHandler implements ErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncomingEventListener.class);

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ActiveMQErrorHandler(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void handleError(Throwable throwable) {
        LOGGER.error("ActiveMQ Error", throwable);

        final JsonResult<WebSocketDto> jsonMessage = new JsonResult<>(new WebSocketDto(Action.ERROR.toString() + " " + throwable.toString()));
        simpMessagingTemplate.convertAndSend("/server/list", jsonMessage);
    }

}
