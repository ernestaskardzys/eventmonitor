package info.ernestas.eventmonitor.service.mapper;

import info.ernestas.eventmonitor.AbstractTest;
import info.ernestas.eventmonitor.dao.entity.Version;
import info.ernestas.eventmonitor.model.dto.VersionDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VersionVersionDtoMapperTest extends AbstractTest {

    private VersionVersionDtoMapper versionVersionDtoMapper;

    @Before
    public void setUp() {
        versionVersionDtoMapper = new VersionVersionDtoMapper();
    }

    @Test
    public void testConvertEntityToDto() {
        Version version = getVersion();

        VersionDto versionDto = versionVersionDtoMapper.convertEntityToDto(version);

        assertEquals(version.getVersion(), versionDto.getVersion());
        assertEquals(version.getName(), versionDto.getName());
        assertEquals(version.getAuthor(), versionDto.getAuthor());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConvertDtoToEntity() {
        versionVersionDtoMapper.convertDtoToEntity(null);
    }

}