package info.ernestas.eventmonitor.dao;

import info.ernestas.eventmonitor.dao.entity.Version;
import info.ernestas.eventmonitor.dao.repository.VersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class VersionDaoImpl implements VersionDao {

    private VersionRepository versionRepository;

    @Autowired
    public VersionDaoImpl(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    @Override
    public long count() {
        return versionRepository.count();
    }

    @Override
    public Version findVersion() {
        return versionRepository.findFirst1ByOrderByVersionDesc();
    }

}
