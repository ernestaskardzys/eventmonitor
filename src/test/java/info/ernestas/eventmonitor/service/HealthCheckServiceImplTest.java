package info.ernestas.eventmonitor.service;

import info.ernestas.eventmonitor.dao.VersionDao;
import info.ernestas.eventmonitor.model.dto.HealthCheckInfoDto;
import org.apache.activemq.broker.BrokerService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HealthCheckServiceImplTest {

    private BrokerService brokerService;

    private HealthCheckServiceImpl healthCheckService;

    private VersionDao versionDao;

    @Before
    public void setUp() {
        brokerService = mock(BrokerService.class);
        versionDao = mock(VersionDao.class);

        healthCheckService = new HealthCheckServiceImpl(brokerService, versionDao);
    }

    @Test
    public void testCheckHealth_withWrongVersionCount() {
        when(versionDao.count()).thenReturn(0L);

        HealthCheckInfoDto healthStatus = healthCheckService.checkHealth();

        assertFalse(healthStatus.isDatabaseWorks());
        assertEquals(0L, healthStatus.getNumberOfVersionsInDatabase());
    }

    @Test
    public void testCheckHealth_withCorrectVersionCount() {
        when(versionDao.count()).thenReturn(1L);

        HealthCheckInfoDto healthStatus = healthCheckService.checkHealth();

        assertTrue(healthStatus.isDatabaseWorks());
        assertEquals(1L, healthStatus.getNumberOfVersionsInDatabase());
    }

    @Test
    public void testCheckHealth_withActiveMQWorking() {
        when(brokerService.isStarted()).thenReturn(true);
        when(brokerService.isStopping()).thenReturn(false);
        when(brokerService.isStopped()).thenReturn(false);

        HealthCheckInfoDto healthStatus = healthCheckService.checkHealth();

        assertTrue(healthStatus.isActiveMQWorks());
    }

    @Test
    public void testCheckHealth_withActiveMQStopping() {
        when(brokerService.isStarted()).thenReturn(false);
        when(brokerService.isStopping()).thenReturn(true);
        when(brokerService.isStopped()).thenReturn(false);

        HealthCheckInfoDto healthStatus = healthCheckService.checkHealth();

        assertFalse(healthStatus.isActiveMQWorks());
    }

    @Test
    public void testCheckHealth_withActiveMQStopped() {
        when(brokerService.isStarted()).thenReturn(false);
        when(brokerService.isStopping()).thenReturn(false);
        when(brokerService.isStopped()).thenReturn(true);

        HealthCheckInfoDto healthStatus = healthCheckService.checkHealth();

        assertFalse(healthStatus.isActiveMQWorks());
    }

    @Test
    public void testCheckHealth_withDatabaseAndActiveMQWorking() {
        when(versionDao.count()).thenReturn(1L);
        when(brokerService.isStarted()).thenReturn(true);
        when(brokerService.isStopping()).thenReturn(false);
        when(brokerService.isStopped()).thenReturn(false);

        HealthCheckInfoDto healthStatus = healthCheckService.checkHealth();

        assertTrue(healthStatus.isDatabaseWorks());
        assertEquals(1L, healthStatus.getNumberOfVersionsInDatabase());
        assertTrue(healthStatus.isActiveMQWorks());
    }

}