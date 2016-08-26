package info.ernestas.eventmonitor.dao.repository;

import info.ernestas.eventmonitor.dao.entity.Event;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface EventRepository extends Repository<Event, Integer> {

    Event save(Event event);

    List<Event> findAll();

    Event delete(Event event);

}
