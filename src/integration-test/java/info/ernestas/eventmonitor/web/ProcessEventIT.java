package info.ernestas.eventmonitor.web;

import com.google.gson.Gson;
import info.ernestas.eventmonitor.AbstractTest;
import info.ernestas.eventmonitor.configs.ApplicationTestConfiguration;
import info.ernestas.eventmonitor.configs.IntegrationTestConfiguration;
import info.ernestas.eventmonitor.embedded.EmbeddedServer;
import info.ernestas.eventmonitor.websocket.IncomingWebSocketRequestsHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationTestConfiguration.class, IntegrationTestConfiguration.class})
@WebAppConfiguration
public class ProcessEventIT extends AbstractTest {

    private static final int TWO_SECONDS = 2000;

    private static final int TWO_MINUTES = 120000;

    private static final int NO_ERROR = 1;

    private MockMvc mockMvc;

    private CountDownLatch errorCounter = new CountDownLatch(NO_ERROR);

    @Value("${tomcat.port}")
    private int tomcatPort;

    @Autowired
    private EmbeddedServer server;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private WebSocketStompClient webSocketStompClient;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test(timeout = TWO_MINUTES)
    public void testProcessEvent() throws Exception {
        IncomingWebSocketRequestsHandler producer = new IncomingWebSocketRequestsHandler(errorCounter);

        webSocketStompClient.connect("ws://localhost:" + tomcatPort + "/events", producer);

        Gson gson = new Gson();

        mockMvc.perform(MockMvcRequestBuilders.put("/rest").contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(getEventDto())));

        // Wait no more than two seconds
        errorCounter.await(TWO_SECONDS, TimeUnit.MILLISECONDS);

        assertEquals(NO_ERROR, errorCounter.getCount());
    }

}
