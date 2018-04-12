package test.procedure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import rest.model.database.Procedure;
import rest.sql.util.SQLObjectBeginEndWrapper;
import test.base.TestAuthenticationBase;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProcedureCreateTest extends TestAuthenticationBase
{
    @Test
    public void whenCreatingProcedure_andSuccessful_thenNoContent_thenItShouldBeInTheDatabase() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        Procedure testProcedure = testProcedure();
        String testProcedureJson = mapper.writeValueAsString(testProcedure);

        mockMvc.perform(
                post("/procedure/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testProcedureJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());

        mockMvc.perform(
                get("/procedure/single")
                        .param("schema", testProcedure.getSchema())
                        .param("procedure", testProcedure.getName())
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.schema").isString())
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.type", equalToIgnoringCase("procedure")))
                .andExpect(jsonPath("$.returnType").doesNotExist())
                .andExpect(jsonPath("$.body").isString())
                .andExpect(jsonPath("$.modified").isString())
                .andExpect(jsonPath("$.paramList", anyOf(nullValue(), hasSize(greaterThan(0)))))
                .andExpect(status().isOk());

        mockMvc.perform(
                delete("/procedure/drop")
                        .param("schema", testProcedure.getSchema())
                        .param("procedure", testProcedure.getName())
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenCreatingProcedure_andInvalidSql_thenConflict() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        Procedure testProcedure = new Procedure();
        testProcedure.setSchema("teszt_schema");
        testProcedure.setName("test_procedure");
        testProcedure.setType("procedure");
        testProcedure.setBody("invalid_body");
        String testProcedureJson = mapper.writeValueAsString(testProcedure);

        mockMvc.perform(
                post("/procedure/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testProcedureJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isConflict());
    }

    @Test
    public void whenCreatingProcedure_andInvalidParams_thenBadRequest() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        Procedure testProcedure = new Procedure();
        testProcedure.setSchema("teszt_schema");
        testProcedure.setName("test_procedure");
        String testProcedureJson = mapper.writeValueAsString(testProcedure);

        mockMvc.perform(
                post("/procedure/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testProcedureJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isBadRequest());
    }

    private Procedure testProcedure()
    {
        String testSchemaName = "teszt_schema";
        String testProcedureName = "test_procedure";

        Procedure testProcedure = new Procedure();
        testProcedure.setSchema(testSchemaName);
        testProcedure.setName(testProcedureName);
        testProcedure.setType("procedure");
        testProcedure.setBody("SELECT 1 FROM DUAL;");

        return testProcedure;
    }
}
