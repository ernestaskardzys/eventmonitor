package info.ernestas.eventmonitor.dao.repository;

import info.ernestas.eventmonitor.dao.entity.Event;
import org.springframework.data.repository.Repository;

public interface EventRepository extends Repository<Event, Integer> {

    Event save(Event event);

    Event delete(Event event);

}
