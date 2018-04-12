package test.procedure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import rest.model.database.Procedure;
import test.base.TestAuthenticationBase;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProcedureDeleteTest extends TestAuthenticationBase
{
    String testSchemaName = "teszt_schema";
    String testProcedureName = "test_procedure";


    @Test
    public void whenDeletingProcedure_andSuccessful_thenNoContent() throws Exception
    {
        createTestProcedure();
        mockMvc.perform(
                delete("/procedure/drop")
                        .param("schema", testSchemaName)
                        .param("procedure", testProcedureName)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenDeletingProcedure_andInvalidParams_thenBadRequest() throws Exception
    {
        mockMvc.perform(
                delete("/procedure/drop")
                        .param("schema", testSchemaName)
                        .headers(getAuthHeaders()))
                .andExpect(status().isBadRequest());
    }

    private void createTestProcedure() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        Procedure testProcedure = new Procedure();
        testProcedure.setSchema(testSchemaName);
        testProcedure.setName(testProcedureName);
        testProcedure.setType("procedure");
        testProcedure.setBody("SELECT 11 FROM DUAL;");
        String testProcedureJson = mapper.writeValueAsString(testProcedure);

        mockMvc.perform(
                post("/procedure/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testProcedureJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }
}
