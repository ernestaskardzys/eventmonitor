package info.ernestas.eventmonitor.embedded;

import org.springframework.web.context.WebApplicationContext;

public interface EmbeddedServer {

    void deployConfig(WebApplicationContext cxt);

    void start() throws Exception;

    void stop() throws Exception;

}