package test.custom;

import test.base.TestAuthenticationBase;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomTest extends TestAuthenticationBase
{
    @Test
    public void customSql_shouldReturnQueryResults_thenOk() throws Exception
    {
        String sql = "SELECT * FROM INFORMATION_SCHEMA.TABLES";
        mockMvc.perform(
                post("/custom")
                        .content(sql)
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isOk());
    }

    @Test
    public void whenInvalidSql_thenConflict() throws Exception
    {
        String sql = "SELECT * FR fsdh";
        mockMvc.perform(
                post("/custom")
                        .content(sql)
                        .headers(getAuthHeaders()))
                .andExpect(status().isConflict());
    }
}
