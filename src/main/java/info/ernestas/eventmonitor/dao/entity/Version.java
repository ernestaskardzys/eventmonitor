package info.ernestas.eventmonitor.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "versions")
public class Version implements Serializable {

    @Id
    @Column(name = "version", unique = true, nullable = false, updatable = false)
    private String version;

    @Column(name = "name")
    private String name;

    @Column(name = "author")
    private String author;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
