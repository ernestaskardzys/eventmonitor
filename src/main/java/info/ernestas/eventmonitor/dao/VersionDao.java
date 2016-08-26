package info.ernestas.eventmonitor.dao;

import info.ernestas.eventmonitor.dao.entity.Version;

public interface VersionDao {

    long count();

    Version findVersion();

}
