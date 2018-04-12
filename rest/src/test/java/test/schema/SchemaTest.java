package test.schema;

import test.base.TestAuthenticationBase;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SchemaTest extends TestAuthenticationBase
{
    @Test
    public void whenRequestingSchemas_thenReturnSchemaArray() throws Exception
    {
        mockMvc.perform(
                get("/schema/all")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isOk());
    }

    @Test
    public void whenCreatingSchema_thenNoContent() throws Exception
    {
        mockMvc.perform(
                post("/schema/create")
                        .param("schema","test_create_schema")
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());

        mockMvc.perform(
                delete("/schema/drop")
                        .param("schema","test_create_schema")
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }
}
