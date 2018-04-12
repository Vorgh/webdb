package test.trigger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import rest.model.database.Trigger;
import rest.sql.util.SQLObjectBeginEndWrapper;
import test.base.TestAuthenticationBase;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TriggerCreateTest extends TestAuthenticationBase
{
    @Autowired
    SQLObjectBeginEndWrapper beginEndWrapper;

    @Test
    public void whenCreatingTrigger_andSuccessful_thenNoContent_thenItShouldBeInTheDatabase() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        Trigger testTrigger = testTrigger();
        String testTriggerJson = mapper.writeValueAsString(testTrigger);

        mockMvc.perform(
                post("/trigger/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testTriggerJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());

        mockMvc.perform(
                get("/trigger/single")
                        .param("schema", testTrigger.schema)
                        .param("trigger", testTrigger.name)
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.schema", equalToIgnoringCase(testTrigger.schema)))
                .andExpect(jsonPath("$.name", equalToIgnoringCase(testTrigger.name)))
                .andExpect(jsonPath("$.eventType", equalToIgnoringCase(testTrigger.eventType)))
                .andExpect(jsonPath("$.eventSchema", equalToIgnoringCase(testTrigger.eventSchema)))
                .andExpect(jsonPath("$.eventTable", equalToIgnoringCase(testTrigger.eventTable)))
                .andExpect(jsonPath("$.triggerBody", equalToIgnoringCase(beginEndWrapper.wrap(testTrigger.triggerBody))))
                .andExpect(status().isOk());

        mockMvc.perform(
                delete("/trigger/drop")
                        .param("schema", testTrigger.schema)
                        .param("trigger", testTrigger.name)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenCreatingTrigger_andInvalidSql_thenConflict() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        Trigger testTrigger = this.testTrigger();
        testTrigger.triggerBody = "invalid_body";
        String testTriggerJson = mapper.writeValueAsString(testTrigger);

        mockMvc.perform(
                post("/trigger/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testTriggerJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isConflict());
    }

    @Test
    public void whenCreatingTrigger_andInvalidParams_thenBadRequest() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        Trigger testTrigger = new Trigger();
        testTrigger.schema = "teszt_schema";
        String testTriggerJson = mapper.writeValueAsString(testTrigger);

        mockMvc.perform(
                post("/trigger/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testTriggerJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreatingTrigger_andSchemaDoesntExist_thenNotFound() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        Trigger testTrigger = this.testTrigger();
        testTrigger.schema = "not_existing_schema";
        String testTriggerJson = mapper.writeValueAsString(testTrigger);

        mockMvc.perform(
                post("/trigger/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testTriggerJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNotFound());
    }

    private Trigger testTrigger()
    {
        String testSchemaName = "teszt_schema";
        String testTriggerName = "test_trigger";
        String testEventTableName = "trigger_test_table_1";
        String testModifiableTable = "trigger_test_table_2";

        Trigger testTrigger = new Trigger();
        testTrigger.schema = testSchemaName;
        testTrigger.name = testTriggerName;
        testTrigger.eventSchema = testSchemaName;
        testTrigger.eventTable = testEventTableName;
        testTrigger.timing = "BEFORE";
        testTrigger.eventType = "INSERT";
        testTrigger.triggerBody = "INSERT INTO " + testModifiableTable + " (`column`) VALUES('test_trigger_run');";

        return testTrigger;
    }
}
