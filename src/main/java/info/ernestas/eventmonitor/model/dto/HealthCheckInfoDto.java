package info.ernestas.eventmonitor.model.dto;

public class HealthCheckInfoDto {

    private boolean databaseWorks;

    private long numberOfVersionsInDatabase;

    private boolean activeMQWorks;

    public boolean isDatabaseWorks() {
        return databaseWorks;
    }

    public void setDatabaseWorks(boolean databaseWorks) {
        this.databaseWorks = databaseWorks;
    }

    public long getNumberOfVersionsInDatabase() {
        return numberOfVersionsInDatabase;
    }

    public void setNumberOfVersionsInDatabase(long numberOfVersionsInDatabase) {
        this.numberOfVersionsInDatabase = numberOfVersionsInDatabase;
    }

    public boolean isActiveMQWorks() {
        return activeMQWorks;
    }

    public void setActiveMQWorks(boolean activeMQWorks) {
        this.activeMQWorks = activeMQWorks;
    }

}
