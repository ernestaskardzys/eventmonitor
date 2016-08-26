package info.ernestas.eventmonitor.model.dto;

public class ErrorInformationDto {

    private String exception;

    private String uri;

    public ErrorInformationDto(String exception, String uri) {
        this.exception = exception;
        this.uri = uri;
    }

    public String getException() {
        return exception;
    }

    public String getUri() {
        return uri;
    }

}
