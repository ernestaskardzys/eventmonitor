package info.ernestas.eventmonitor.service;

import info.ernestas.eventmonitor.AbstractTest;
import info.ernestas.eventmonitor.dao.VersionDao;
import info.ernestas.eventmonitor.dao.entity.Version;
import info.ernestas.eventmonitor.model.dto.VersionDto;
import info.ernestas.eventmonitor.service.mapper.VersionVersionDtoMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VersionServiceImplTest extends AbstractTest {

    private VersionDao versionDao;

    private VersionServiceImpl versionService;

    private VersionVersionDtoMapper versionVersionDtoMapper;

    @Before
    public void setUp() {
        versionDao = mock(VersionDao.class);
        versionVersionDtoMapper = new VersionVersionDtoMapper();

        when(versionDao.findVersion()).thenReturn(getVersion());

        versionService = new VersionServiceImpl(versionDao, versionVersionDtoMapper);
    }

    @Test
    public void testGetVersionInformation() {
        VersionDto versionDto = versionService.getVersionInformation();

        Version version = getVersion();
        assertEquals(version.getVersion(), versionDto.getVersion());
        assertEquals(version.getName(), versionDto.getName());
        assertEquals(version.getAuthor(), versionDto.getAuthor());
    }

}