package info.ernestas.eventmonitor.dao;

import info.ernestas.eventmonitor.dao.entity.Event;
import info.ernestas.eventmonitor.dao.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventDaoImpl implements EventDao {

    private EventRepository eventRepository;

    @Autowired
    public EventDaoImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event delete(Event event) {
        return eventRepository.delete(event);
    }

}
