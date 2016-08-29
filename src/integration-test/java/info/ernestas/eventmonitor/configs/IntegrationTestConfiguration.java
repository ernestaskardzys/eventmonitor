package info.ernestas.eventmonitor.configs;

import info.ernestas.eventmonitor.embedded.EmbeddedServer;
import info.ernestas.eventmonitor.embedded.TomcatEmbeddedServer;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.JettyXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class IntegrationTestConfiguration {

    private static final int MAX_THREADS = 10;

    @Value("${tomcat.port}")
    private int tomcatPort;

    @Bean
    public SockJsClient sockJsClient() throws Exception {
        HttpClient jettyHttpClient = new HttpClient();
        jettyHttpClient.setMaxConnectionsPerDestination(MAX_THREADS);
        jettyHttpClient.setExecutor(new QueuedThreadPool(MAX_THREADS));
        jettyHttpClient.start();

        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new JettyXhrTransport(jettyHttpClient));

        return new SockJsClient(transports);
    }

    @Bean
    public WebSocketStompClient webSocketStompClient(SockJsClient sockJsClient) {
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setTaskScheduler(new DefaultManagedTaskScheduler());

        return stompClient;
    }

    @Bean
    public EmbeddedServer embeddedServer() throws Exception {
        String tempDirectory = System.getProperty("java.io.tmpdir");

        String tomcatBaseDirectory = tempDirectory + "/tomcat." + tomcatPort;
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(WebSocketConfiguration.class);

        EmbeddedServer server = new TomcatEmbeddedServer(tomcatPort, tomcatBaseDirectory);
        server.deployConfig(applicationContext);
        server.start();

        return server;
    }

}
