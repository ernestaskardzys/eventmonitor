package info.ernestas.eventmonitor.dao;

import info.ernestas.eventmonitor.dao.entity.Event;

public interface EventDao {

    Event save(Event event);

    Event delete(Event event);

}
