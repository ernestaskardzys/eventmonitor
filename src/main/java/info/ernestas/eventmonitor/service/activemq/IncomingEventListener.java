package info.ernestas.eventmonitor.service.activemq;

import info.ernestas.eventmonitor.model.Action;
import info.ernestas.eventmonitor.model.EventMonitorException;
import info.ernestas.eventmonitor.model.dto.EventDto;
import info.ernestas.eventmonitor.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
public class IncomingEventListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncomingEventListener.class);

    private EventService eventService;

    @Autowired
    public IncomingEventListener(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            if (objectMessage.getObject() instanceof EventDto) {
                EventDto eventDto = (EventDto) objectMessage.getObject();

                eventDto = eventService.processEvent(eventDto, Action.INSERT);

                eventDto = eventService.processEvent(eventDto, Action.UPDATE);

                eventService.processEvent(eventDto, Action.DELETE);
            }
        } catch (Exception exception) {
            final String errorMessage = "JMS error ";
            LOGGER.error(errorMessage, exception);
            throw new EventMonitorException(errorMessage, exception);
        }
    }

}
