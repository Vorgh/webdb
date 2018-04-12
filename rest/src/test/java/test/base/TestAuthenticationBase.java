package test.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfigMXBean;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import rest.Application;
import rest.model.connection.ConnectionAuthInfo;

import javax.servlet.Filter;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
@WebAppConfiguration
public abstract class TestAuthenticationBase
{
    @Autowired
    WebApplicationContext wac;

    @Autowired
    Filter springSecurityFilterChain;

    @Autowired
    HikariDataSource dataSource;

    @Autowired
    HikariConfigMXBean hikariConfigMXBean;

    protected static MockMvc mockMvc;
    protected static ConnectionAuthInfo authInfo;
    protected static String accessToken;
    private static boolean initialized = false;

    @Before
    public void baseAuthSetup() throws Exception
    {
        if (!initialized)
        {
            mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                    .addFilter(springSecurityFilterChain)
                    .build();

            authInfo = new ConnectionAuthInfo();
            authInfo.setUsername("test");
            authInfo.setPassword("test");
            authInfo.setUrl("jdbc:mysql://localhost:3306");
            setLoggedInUser(authInfo);

            initialized = true;
        }
    }

    protected void setLoggedInUser(ConnectionAuthInfo authInfo) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();

        hikariConfigMXBean.setUsername(authInfo.getUsername());
        hikariConfigMXBean.setPassword(authInfo.getPassword());
        /*dataSource.setUsername(authInfo.getUsername());
        dataSource.setPassword(authInfo.getPassword());
        dataSource.setJdbcUrl(authInfo.getUrl());*/

        String json = mapper.writeValueAsString(authInfo);

        mockMvc.perform(post("/connection/connectionAuth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        String basicAuth = Base64.getEncoder().encodeToString((authInfo.getUsername() + ":" + authInfo.getPassword()).getBytes());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("url", authInfo.getUrl());
        params.add("username", authInfo.getUsername());
        params.add("password", authInfo.getPassword());

        MvcResult tokenResult = mockMvc.perform(post("/oauth/token")
                .params(params)
                .header("Authorization", "Basic " + basicAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.access_token").isNotEmpty())
                .andExpect(jsonPath("$.refresh_token").exists())
                .andExpect(jsonPath("$.refresh_token").isNotEmpty())
                .andReturn();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        accessToken = jsonParser.parseMap(tokenResult.getResponse().getContentAsString()).get("access_token").toString();
    }

    protected static HttpHeaders getAuthHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        return headers;
    }
}
