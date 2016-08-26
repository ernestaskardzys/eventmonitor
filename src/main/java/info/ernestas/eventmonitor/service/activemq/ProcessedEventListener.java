package info.ernestas.eventmonitor.service.activemq;

import info.ernestas.eventmonitor.model.EventMonitorException;
import info.ernestas.eventmonitor.model.EventStatusChange;
import info.ernestas.eventmonitor.model.JsonResult;
import info.ernestas.eventmonitor.model.dto.WebSocketDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
public class ProcessedEventListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessedEventListener.class);

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ProcessedEventListener(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            if (objectMessage.getObject() instanceof EventStatusChange) {
                EventStatusChange eventStatusChange = (EventStatusChange) objectMessage.getObject();

                informUserAboutStatusChange(eventStatusChange);
            }
        } catch (Exception exception) {
            final String errorMessage = "JMS error ";
            LOGGER.error(errorMessage, exception);
            throw new EventMonitorException(errorMessage, exception);
        }
    }

    private void informUserAboutStatusChange(EventStatusChange eventStatusChange) {
        JsonResult<WebSocketDto> jsonMessage = new JsonResult<>(new WebSocketDto(eventStatusChange.getAction().toString() + eventStatusChange.getEventDto()));
        simpMessagingTemplate.convertAndSend("/server/list", jsonMessage);
    }

}
