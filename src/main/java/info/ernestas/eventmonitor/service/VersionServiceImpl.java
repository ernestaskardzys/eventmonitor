package info.ernestas.eventmonitor.service;

import info.ernestas.eventmonitor.dao.VersionDao;
import info.ernestas.eventmonitor.dao.entity.Version;
import info.ernestas.eventmonitor.model.dto.VersionDto;
import info.ernestas.eventmonitor.service.mapper.VersionVersionDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VersionServiceImpl implements VersionService {

    private VersionDao versionDao;

    private VersionVersionDtoMapper versionVersionDtoMapper;

    @Autowired
    public VersionServiceImpl(VersionDao versionDao, VersionVersionDtoMapper versionVersionDtoMapper) {
        this.versionDao = versionDao;
        this.versionVersionDtoMapper = versionVersionDtoMapper;
    }

    @Cacheable("versionCache")
    @Override
    public VersionDto getVersionInformation() {
        Version version = versionDao.findVersion();
        return versionVersionDtoMapper.convertEntityToDto(version);
    }

}
