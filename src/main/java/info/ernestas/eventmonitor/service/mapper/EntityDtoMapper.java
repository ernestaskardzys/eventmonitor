package info.ernestas.eventmonitor.service.mapper;

import java.io.Serializable;

public interface EntityDtoMapper<S extends Serializable, T extends Serializable> {

    S convertEntityToDto(T entity);

    T convertDtoToEntity(S dto);

}
