package test.procedure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import rest.model.database.Procedure;
import rest.model.request.Change;
import rest.sql.util.SQLObjectBeginEndWrapper;
import test.base.TestAuthenticationBase;

import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProcedureModifyTest extends TestAuthenticationBase
{
    @Autowired
    private SQLObjectBeginEndWrapper beginEndWrapper;

    private Procedure testProcedure;

    @Before
    public void createTestProcedure() throws Exception
    {
        String testSchemaName = "teszt_schema";
        String testProcedureName = "test_procedure";

        ObjectMapper mapper = new ObjectMapper();
        testProcedure = new Procedure();
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
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenModifyingProcedure_andSuccessful_thenNoContent_thenItShouldChange() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        Change<Procedure> testProcedureChange = testProcedureChange();
        String testChangeJson = mapper.writeValueAsString(testProcedureChange);

        mockMvc.perform(
                put("/procedure/modify")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testChangeJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());

        mockMvc.perform(
                get("/procedure/single")
                        .param("schema", testProcedureChange.to.getSchema())
                        .param("procedure", testProcedureChange.to.getName())
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.schema", equalToIgnoringCase(testProcedureChange.to.getSchema())))
                .andExpect(jsonPath("$.name", equalToIgnoringCase(testProcedureChange.to.getName())))
                .andExpect(jsonPath("$.type", equalToIgnoringCase(testProcedureChange.to.getType())))
                .andExpect(jsonPath("$.body", equalToIgnoringCase(beginEndWrapper.wrap(testProcedureChange.to.getBody()))))
                .andExpect(status().isOk());
    }

    @Test
    public void whenModifyingProcedure_andInvalidSql_thenConflict_andShouldRollback() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();

        Change<Procedure> testProcedureChange = testProcedureChange();
        String originalBody = testProcedureChange.from.getBody();
        testProcedureChange.to.setBody("lsdkgjsdpogkso");
        String testChangeJson = mapper.writeValueAsString(testProcedureChange);

        mockMvc.perform(
                put("/procedure/modify")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testChangeJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isConflict());

        mockMvc.perform(
                get("/procedure/single")
                        .param("schema", testProcedureChange.to.getSchema())
                        .param("procedure", testProcedureChange.to.getName())
                        .headers(getAuthHeaders()))
                .andExpect(jsonPath("$.body", equalToIgnoringCase(beginEndWrapper.wrap(originalBody))))
                .andExpect(status().isOk());
    }

    @Test
    public void whenModifyingProcedure_andInvalidParams_thenBadRequest() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        Change<Procedure> testProcedureChange = testProcedureChange();
        testProcedureChange.from.setSchema(null);
        String testChangeJson = mapper.writeValueAsString(testProcedureChange);

        mockMvc.perform(
                put("/procedure/modify")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testChangeJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isBadRequest());
    }

    @After
    public void deleteTestProcedure() throws Exception
    {
        mockMvc.perform(
                delete("/procedure/drop")
                        .param("schema", testProcedure.getSchema())
                        .param("procedure", testProcedure.getName())
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }

    private Change<Procedure> testProcedureChange()
    {
        String testSchemaName = "teszt_schema";
        String testProcedureName = "test_procedure";

        Procedure testProcedure1 = new Procedure();
        testProcedure1.setSchema(testSchemaName);
        testProcedure1.setName(testProcedureName);
        testProcedure1.setType("procedure");
        testProcedure1.setBody("SELECT 1 FROM DUAL;");

        Procedure testProcedure2 = new Procedure();
        testProcedure2.setSchema(testSchemaName);
        testProcedure2.setName(testProcedureName);
        testProcedure2.setType("procedure");
        testProcedure2.setBody("SELECT 2 FROM DUAL;");

        Change<Procedure> change = new Change<>();
        change.from = testProcedure1;
        change.to = testProcedure2;

        return change;
    }
}
