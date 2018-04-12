package test.connection;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import rest.model.database.Procedure;
import test.base.TestAuthenticationBase;
import org.junit.*;
import org.springframework.http.HttpHeaders;
import rest.model.connection.ConnectionAuthInfo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthTest extends TestAuthenticationBase
{
    @Test
    public void givenNoToken_whenGetSecureRequest_thenUnauthorized() throws Exception
    {
        mockMvc.perform(get("/schema/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenToken_whenGetSecureRequest_thenOk() throws Exception
    {
        mockMvc.perform(get("/schema/all")
                .headers(this.getAuthHeaders()))
                .andExpect(status().isOk());
    }

    @Test
    public void onLogin_whenUserIsSame_thenTokenMustBeSame() throws Exception
    {
        String originalAccessToken = accessToken;
        setLoggedInUser(authInfo);

        Assert.assertEquals(originalAccessToken, accessToken);
    }

    @Test
    public void onLogin_whenUrlIsDifferentButDomainAndUserIsSame_thenTokenMustBeSame() throws Exception
    {
        String originalAccessToken = accessToken;
        ConnectionAuthInfo originalAuthInfo = authInfo;

        ConnectionAuthInfo newAuthInfo = new ConnectionAuthInfo();
        newAuthInfo.setUsername(originalAuthInfo.getUsername());
        newAuthInfo.setPassword(originalAuthInfo.getPassword());
        newAuthInfo.setUrl(originalAuthInfo.getUrl() + "/webdb");

        setLoggedInUser(newAuthInfo);

        Assert.assertEquals(originalAccessToken, accessToken);
    }

    @Test
    public void onLogin_whenUserIsDifferent_thenTokenMustChange() throws Exception
    {
        String originalAccessToken = accessToken;
        ConnectionAuthInfo originalAuthInfo = authInfo;

        ConnectionAuthInfo newAuthInfo = new ConnectionAuthInfo();
        newAuthInfo.setUsername("test2");
        newAuthInfo.setPassword("test2");
        newAuthInfo.setUrl(originalAuthInfo.getUrl());

        setLoggedInUser(newAuthInfo);

        Assert.assertNotEquals(originalAccessToken, accessToken);

        authInfo = originalAuthInfo;
        accessToken = originalAccessToken;
    }

    @Test
    public void onLogout_whenAuthTokenIsWrongOrMissing_thenUnauthorized() throws Exception
    {
        mockMvc.perform(
                post("/connection/logout"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void onLogout_whenSuccessful_thenUserMustBeUnauthorized() throws Exception
    {
        ConnectionAuthInfo originalAuthInfo = authInfo;

        mockMvc.perform(
                post("/connection/logout")
                .headers(getAuthHeaders()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/schema/all"))
                .andExpect(status().isUnauthorized());

        setLoggedInUser(originalAuthInfo);
    }

    @Test
    public void whenSqlQueryWithoutPrivileges_thenForbidden() throws Exception
    {
        String testSchemaName = "webdb";
        String testProcedureName = "privilege_test_procedure";

        ObjectMapper mapper = new ObjectMapper();
        Procedure testProcedure = new Procedure();
        testProcedure.setSchema(testSchemaName);
        testProcedure.setName(testProcedureName);
        testProcedure.setType("procedure");
        testProcedure.setBody("SELECT 1 FROM DUAL;");
        String testProcedureJson = mapper.writeValueAsString(testProcedure);

        mockMvc.perform(
                post("/procedure/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testProcedureJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isForbidden());
    }
}
