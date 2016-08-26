package info.ernestas.eventmonitor.ws;

import com.google.gson.Gson;
import info.ernestas.eventmonitor.AbstractTest;
import info.ernestas.eventmonitor.configs.ApplicationTestConfiguration;
import info.ernestas.eventmonitor.dao.VersionDao;
import info.ernestas.eventmonitor.dao.entity.Version;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTestConfiguration.class })
@WebAppConfiguration
@Transactional
public class ManagementEndpointTest extends AbstractTest {

    private MockMvc mockMvc;

    @Autowired
    private VersionDao versionDao;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testUpload() throws Exception {
        Gson gson = new Gson();

        mockMvc.perform(put("/rest").contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(getEventDto())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status", is("SUCCESS")))
            .andExpect(jsonPath("$.data", is("OK")));
    }

    @Test
    public void testInfo() throws Exception {
        Version version = versionDao.findVersion();

        mockMvc.perform(get("/rest/info").contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.data.version", is(version.getVersion())))
            .andExpect(jsonPath("$.data.name", is(version.getName())))
            .andExpect(jsonPath("$.data.author", is(version.getAuthor())));
    }

    @Test
    public void testGetHealthCheck() throws Exception {
        long versionsInDatabase = versionDao.count();

        mockMvc.perform(get("/rest/health-check").contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.data.databaseWorks", is(true)))
            .andExpect(jsonPath("$.data.numberOfVersionsInDatabase", is((int)versionsInDatabase)))
            .andExpect(jsonPath("$.data.activeMQWorks", is(true)));
    }

}