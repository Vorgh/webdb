package test.trigger;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import rest.model.database.Trigger;
import rest.sql.util.SQLObjectBeginEndWrapper;
import test.base.TestAuthenticationBase;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TriggerGetTest extends TestAuthenticationBase
{
    private static Trigger testTrigger;

    @Autowired
    SQLObjectBeginEndWrapper beginEndWrapper;

    @BeforeClass
    public static void createTestTrigger()
    {
        String testSchemaName = "teszt_schema";
        String testTriggerName = "get_test_trigger";
        String testEventTableName = "trigger_test_table_1";
        String testModifiableTable = "trigger_test_table_2";

        testTrigger = new Trigger();
        testTrigger.schema = testSchemaName;
        testTrigger.name = testTriggerName;
        testTrigger.eventSchema = testSchemaName;
        testTrigger.eventTable = testEventTableName;
        testTrigger.timing = "BEFORE";
        testTrigger.eventType = "INSERT";
        testTrigger.triggerBody = "INSERT INTO " + testModifiableTable + "(`column`) VALUES ('test_trigger_run');";
    }

    @Test
    public void whenRequestingAllTriggers_andSuccessful_thenReturnArray() throws Exception
    {
        mockMvc.perform(
                get("/trigger/all")
                        .param("schema", testTrigger.schema)
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isOk());
    }

    @Test
    public void whenRequestingSingleTrigger_andSuccessful_thenReturnTrigger() throws Exception
    {
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
    }

    @Test
    public void whenTriggerDoesntExist_thenNotFound() throws Exception
    {
        mockMvc.perform(
                get("/trigger/single")
                        .param("schema", "teszt_schema")
                        .param("trigger", "trigger_that_doesnt_exist")
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenSchemaParamIsMissing_thenBadRequest() throws Exception
    {
        mockMvc.perform(
                get("/trigger/single")
                        .param("trigger", testTrigger.name)
                        .headers(getAuthHeaders()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenTriggerParamIsMissing_thenBadRequest() throws Exception
    {
        mockMvc.perform(
                get("/trigger/single")
                        .param("schema", "teszt_schema")
                        .headers(getAuthHeaders()))
                .andExpect(status().isBadRequest());
    }


}
