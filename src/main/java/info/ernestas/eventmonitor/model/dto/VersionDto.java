package info.ernestas.eventmonitor.model.dto;

import java.io.Serializable;

public class VersionDto implements Serializable {

    private String version;

    private String name;

    private String author;

    public VersionDto(String version, String name, String author) {
        this.version = version;
        this.name = name;
        this.author = author;
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "version=" + version + ", name=" + name + ", author=" + author;
    }

}
