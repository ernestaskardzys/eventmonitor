package info.ernestas.eventmonitor.service;

import info.ernestas.eventmonitor.dao.VersionDao;
import info.ernestas.eventmonitor.model.dto.HealthCheckInfoDto;
import org.apache.activemq.broker.BrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckServiceImpl implements HealthCheckService {

    private BrokerService brokerService;

    private VersionDao versionDao;

    @Autowired
    public HealthCheckServiceImpl(BrokerService brokerService, VersionDao versionDao) {
        this.brokerService = brokerService;
        this.versionDao = versionDao;
    }

    @Override
    public HealthCheckInfoDto checkHealth() {
        HealthCheckInfoDto result = new HealthCheckInfoDto();
        Long count = versionDao.count();

        if (count != null && count > 0) {
            result.setDatabaseWorks(true);
            result.setNumberOfVersionsInDatabase(count);
        }

        result.setActiveMQWorks(activeMQWorks());

        return result;
    }

    private boolean activeMQWorks() {
        return brokerService.isStarted() && !brokerService.isStopped() && !brokerService.isStopping();
    }

}
