package info.ernestas.eventmonitor.web;

import com.google.gson.Gson;
import info.ernestas.eventmonitor.AbstractTest;
import info.ernestas.eventmonitor.configs.ApplicationTestConfiguration;
import info.ernestas.eventmonitor.embedded.EmbeddedServer;
import info.ernestas.eventmonitor.embedded.TomcatEmbeddedServer;
import info.ernestas.eventmonitor.websocket.IncomingWebSocketRequestsHandler;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.JettyXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationTestConfiguration.class})
@WebAppConfiguration
public class ProcessEventIT extends AbstractTest {

    private static final int TOMCAT_PORT = 10000;

    private static final int TWO_SECONDS = 2000;

    private static final int SIX_SECONDS = 6000;

    private static final int NO_ERROR = 1;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private MockMvc mockMvc;

    private EmbeddedServer server;

    private CountDownLatch errorCounter = new CountDownLatch(NO_ERROR);

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        server = new TomcatEmbeddedServer(TOMCAT_PORT, testFolder.getRoot().getAbsolutePath());
        server.deployConfig(webApplicationContext);
        server.start();

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test(timeout = SIX_SECONDS)
    public void testProcessEvent() throws Exception {
        WebSocketStompClient stompClient = new WebSocketStompClient(getSockJsClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setTaskScheduler(new DefaultManagedTaskScheduler());

        IncomingWebSocketRequestsHandler producer = new IncomingWebSocketRequestsHandler(errorCounter);
        stompClient.connect("ws://localhost:" + TOMCAT_PORT + "/events", producer);

        Gson gson = new Gson();

        mockMvc.perform(MockMvcRequestBuilders.put("/rest").contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(getEventDto())));

        // Wait no more than two seconds
        errorCounter.await(TWO_SECONDS, TimeUnit.MILLISECONDS);

        assertEquals(1, errorCounter.getCount());
    }

    private SockJsClient getSockJsClient() throws Exception {
        HttpClient jettyHttpClient = new HttpClient();
        jettyHttpClient.setMaxConnectionsPerDestination(10);
        jettyHttpClient.setExecutor(new QueuedThreadPool(10));
        jettyHttpClient.start();

        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new JettyXhrTransport(jettyHttpClient));

        return new SockJsClient(transports);
    }

}
