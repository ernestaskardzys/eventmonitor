package info.ernestas.eventmonitor.dao.repository;

import info.ernestas.eventmonitor.dao.entity.Version;
import org.springframework.data.repository.Repository;

public interface VersionRepository extends Repository<Version, String> {

    Long count();

    Version findFirst1ByOrderByVersionDesc();

}
