package info.ernestas.eventmonitor.service.mapper;

import info.ernestas.eventmonitor.dao.entity.Version;
import info.ernestas.eventmonitor.model.dto.VersionDto;
import org.springframework.stereotype.Component;

@Component
public class VersionVersionDtoMapper implements EntityDtoMapper<VersionDto, Version> {

    @Override
    public VersionDto convertEntityToDto(Version entity) {
        return new VersionDto(entity.getVersion(), entity.getName(), entity.getAuthor());
    }

    @Override
    public Version convertDtoToEntity(VersionDto dto) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

}
