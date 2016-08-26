package info.ernestas.eventmonitor.model;

public class EventMonitorException extends RuntimeException {

    public EventMonitorException() {
    }

    public EventMonitorException(String message) {
        super(message);
    }

    public EventMonitorException(String message, Throwable cause) {
        super(message, cause);
    }
}
