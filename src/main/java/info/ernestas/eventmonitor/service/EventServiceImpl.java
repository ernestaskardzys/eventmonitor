package info.ernestas.eventmonitor.service;

import info.ernestas.eventmonitor.dao.EventDao;
import info.ernestas.eventmonitor.dao.entity.Event;
import info.ernestas.eventmonitor.model.Action;
import info.ernestas.eventmonitor.model.EventMonitorException;
import info.ernestas.eventmonitor.model.EventStatusChange;
import info.ernestas.eventmonitor.model.dto.EventDto;
import info.ernestas.eventmonitor.service.mapper.EventEventDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

    private static final ThreadLocalRandom RANDOM_NUMBER_GENERATOR = ThreadLocalRandom.current();

    private static final int MAX_USER_ID = 10000;

    private EventDao eventDao;

    private JmsTemplate jmsTemplate;

    private EventEventDtoMapper eventEventDtoMapper;

    private Boolean enableDelay;

    private Integer eventProcessingInitialTime;

    private Integer eventProcessingMaxRandomTime;

    private String incomingEventQueue;

    private String processedEventQueue;

    @Autowired
    public EventServiceImpl(EventDao eventDao, EventEventDtoMapper eventEventDtoMapper, JmsTemplate jmsTemplate) {
        this.eventDao = eventDao;
        this.eventEventDtoMapper = eventEventDtoMapper;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void saveIncomingEvent(EventDto eventDto) {
        MessageCreator messageCreator = session -> session.createObjectMessage(eventDto);
        jmsTemplate.send(incomingEventQueue, messageCreator);
    }

    @Override
    public EventDto processEvent(EventDto eventDto, Action action) {
        Event event = eventEventDtoMapper.convertDtoToEntity(eventDto);
        switch (action) {
            case INSERT: {
                event.setUpdatedBy(RANDOM_NUMBER_GENERATOR.nextInt(MAX_USER_ID));
                event.setTimestamp(LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute());
                event = eventDao.save(event);
                saveProcessedEvent(eventEventDtoMapper.convertEntityToDto(event), action);
                break;
            }
            case UPDATE: {
                event = updateEvent(event);
                saveProcessedEvent(eventDto, action);
                break;
            }
            case DELETE: {
                saveProcessedEvent(eventDto, action);
                event = eventDao.delete(event);
                break;
            }
            default: {
                saveProcessedEvent(eventDto, Action.ERROR);
                break;
            }
        }

        return eventEventDtoMapper.convertEntityToDto(event);
    }

    private void saveProcessedEvent(EventDto eventDto, Action action) {
        MessageCreator messageCreator = session -> session.createObjectMessage(new EventStatusChange(eventDto, action));
        jmsTemplate.send(processedEventQueue, messageCreator);
    }

    private Event updateEvent(Event event) {
        if (enableDelay) {
            try {
                // Simulating event processing - it should be different each invocation
                Thread.sleep(eventProcessingInitialTime + RANDOM_NUMBER_GENERATOR.nextInt(eventProcessingMaxRandomTime));
            } catch (InterruptedException e) {
                final String message = "Error in delay";
                LOGGER.error(message, e);
                throw new EventMonitorException(message, e);
            }
        }

        return eventDao.save(event);
    }

    @Value("${event.processingTime.enableDelay}")
    public void setEnableDelay(Boolean enableDelay) {
        this.enableDelay = enableDelay;
    }

    @Value("${event.processingTime.initial}")
    public void setEventProcessingInitialTime(Integer eventProcessingInitialTime) {
        this.eventProcessingInitialTime = eventProcessingInitialTime;
    }

    @Value("${event.processingTime.maxRandom}")
    public void setEventProcessingMaxRandomTime(Integer eventProcessingMaxRandomTime) {
        this.eventProcessingMaxRandomTime = eventProcessingMaxRandomTime;
    }

    @Value("${activemq.queue.name.incomingEventQueue}")
    public void setIncomingEventQueue(String incomingEventQueue) {
        this.incomingEventQueue = incomingEventQueue;
    }

    @Value("${activemq.queue.name.processedEventQueue}")
    public void setProcessedEventQueue(String processedEventQueue) {
        this.processedEventQueue = processedEventQueue;
    }

}
