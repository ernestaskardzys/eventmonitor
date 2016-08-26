package info.ernestas.eventmonitor.model.dto;

import java.io.Serializable;

public class EventDto implements Serializable {

    private Integer id;

    private String name;

    private String timestamp;

    private Integer updatedBy;

    public EventDto() {

    }

    public EventDto(Integer id, String name, String timestamp, Integer updatedBy) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
        this.updatedBy = updatedBy;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public String toString() {
        return " id=" + id + ", timestamp=" + timestamp + ", updatedBy=" + updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventDto eventDto = (EventDto) o;

        if (!id.equals(eventDto.id)) {
            return false;
        }
        if (!name.equals(eventDto.name)) {
            return false;
        }
        if (!timestamp.equals(eventDto.timestamp)) {
            return false;
        }

        return updatedBy.equals(eventDto.updatedBy);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + timestamp.hashCode();
        result = 31 * result + updatedBy.hashCode();
        return result;
    }
}
