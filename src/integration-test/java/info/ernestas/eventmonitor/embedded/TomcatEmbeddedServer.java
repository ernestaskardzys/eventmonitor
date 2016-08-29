package info.ernestas.eventmonitor.embedded;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.websocket.server.WsContextListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class TomcatEmbeddedServer implements EmbeddedServer {

    private static final String DISPATCHER_SERVLET = "dispatcherServlet";

    private static final String CONTEXT_ROOT = "/";

    private Tomcat tomcatServer;

    private String tomcatBaseDirectory;

    private Context context;

    public TomcatEmbeddedServer(int port, String tomcatBaseDirectory) {
        this.tomcatBaseDirectory = tomcatBaseDirectory;

        Connector connector = new Connector(Http11NioProtocol.class.getName());
        connector.setPort(port);

        tomcatServer = new Tomcat();
        tomcatServer.setBaseDir(tomcatBaseDirectory + "/tomcat." + port);
        tomcatServer.setPort(port);
        tomcatServer.getService().addConnector(connector);
        tomcatServer.setConnector(connector);
    }

    @Override
    public void deployConfig(WebApplicationContext webApplicationContext) {
        context = tomcatServer.addContext(CONTEXT_ROOT, tomcatBaseDirectory);
        context.addApplicationListener(WsContextListener.class.getName());
        Tomcat.addServlet(context, DISPATCHER_SERVLET, new DispatcherServlet(webApplicationContext));
        context.addServletMapping(CONTEXT_ROOT, DISPATCHER_SERVLET);
    }

    @Override
    public void start() throws Exception {
        tomcatServer.start();
    }

    @Override
    public void stop() throws Exception {
        if (context != null) {
            tomcatServer.getHost().removeChild(context);
        }

        tomcatServer.stop();

        tomcatServer.destroy();
    }

}
