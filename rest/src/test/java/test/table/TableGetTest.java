package test.table;

import org.junit.Test;
import org.springframework.http.MediaType;
import test.base.TestAuthenticationBase;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TableGetTest extends TestAuthenticationBase
{
    @Test
    public void whenRequestingAllTables_andSuccessful_thenReturnArray() throws Exception
    {
        mockMvc.perform(
                get("/table/metadata/all")
                        .param("schema", "teszt_schema")
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isOk());
    }

    @Test
    public void whenRequestingAllTables_andSchemaDoesntExist_thenNotFound() throws Exception
    {
        mockMvc.perform(
                get("/table/metadata/all")
                        .param("schema", "not_existing_schema")
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenRequestingSingleTable_andSuccessful_thenReturnTable() throws Exception
    {
        mockMvc.perform(
                get("/table/metadata/single")
                        .param("schema", "teszt_schema")
                        .param("table", "get_test_table")
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.schema").isString())
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.type", equalToIgnoringCase("BASE TABLE")))
                .andExpect(jsonPath("$.engine").isString())
                .andExpect(jsonPath("$.creationDate").isString())
                .andExpect(jsonPath("$.collation").isString())
                .andExpect(status().isOk());
    }

    @Test
    public void whenTableDoesntExist_thenNotFound() throws Exception
    {
        mockMvc.perform(
                get("/table/metadata/single")
                        .param("schema", "teszt_schema")
                        .param("table", "table_that_doesnt_exist")
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenSchemaParamIsMissing_thenBadRequest() throws Exception
    {
        mockMvc.perform(
                get("/table/metadata/single")
                        .param("table", "get_test_table")
                        .headers(getAuthHeaders()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenTableParamIsMissing_thenBadRequest() throws Exception
    {
        mockMvc.perform(
                get("/table/metadata/single")
                        .param("schema", "teszt_schema")
                        .headers(getAuthHeaders()))
                .andExpect(status().isBadRequest());
    }
}
