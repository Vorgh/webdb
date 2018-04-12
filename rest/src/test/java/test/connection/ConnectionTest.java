package test.connection;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import rest.Application;
import rest.model.connection.ConnectionAuthInfo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
@WebAppConfiguration
public class ConnectionTest
{
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void whenCorrectLoginCredentials_thenOk() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        ConnectionAuthInfo authInfo = new ConnectionAuthInfo();
        authInfo.setUsername("test");
        authInfo.setPassword("test");
        authInfo.setUrl("jdbc:mysql://localhost:3306");

        String json = mapper.writeValueAsString(authInfo);

        this.mockMvc.perform(
                post("/connection/connectionAuth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void whenInvalidJdbcUrl_thenBadRequest() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        ConnectionAuthInfo authInfo = new ConnectionAuthInfo();
        authInfo.setUsername("test");
        authInfo.setPassword("test");
        authInfo.setUrl("asd123");

        String json = mapper.writeValueAsString(authInfo);

        this.mockMvc.perform(
                post("/connection/connectionAuth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenInvalidLoginCredentials_thenBadRequest() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        ConnectionAuthInfo authInfo = new ConnectionAuthInfo();
        authInfo.setUsername("bad_user");
        authInfo.setPassword("bad_password");
        authInfo.setUrl("jdbc:mysql://localhost:3306");

        String json = mapper.writeValueAsString(authInfo);

        this.mockMvc.perform(
                post("/connection/connectionAuth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenMissingLoginCredentials_thenBadRequest() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        ConnectionAuthInfo authInfo = new ConnectionAuthInfo();
        //Missing username
        authInfo.setPassword("bad_password");
        authInfo.setUrl("jdbc:mysql://localhost:3306");

        String json = mapper.writeValueAsString(authInfo);

        this.mockMvc.perform(
                post("/connection/connectionAuth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
