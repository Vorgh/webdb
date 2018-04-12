package test.trigger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.http.MediaType;
import rest.model.database.Trigger;
import test.base.TestAuthenticationBase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TriggerDeleteTest extends TestAuthenticationBase
{
    private String testSchemaName = "teszt_schema";
    private String testTriggerName = "test_trigger";
    private String testEventTableName = "trigger_test_table_1";
    private String testModifiableTable = "trigger_test_table_2";

    @Test
    public void whenDeletingTrigger_andSuccessful_thenNoContent() throws Exception
    {
        createTestTrigger();
        mockMvc.perform(
                delete("/trigger/drop")
                        .param("schema", testSchemaName)
                        .param("trigger", testTriggerName)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenDeletingTrigger_andInvalidParams_thenBadRequest() throws Exception
    {
        mockMvc.perform(
                delete("/trigger/drop")
                        .param("schema", testSchemaName)
                        .headers(getAuthHeaders()))
                .andExpect(status().isBadRequest());
    }

    private void createTestTrigger() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        Trigger testTrigger = new Trigger();
        testTrigger.schema = testSchemaName;
        testTrigger.name = testTriggerName;
        testTrigger.eventSchema = testSchemaName;
        testTrigger.eventTable = testEventTableName;
        testTrigger.timing = "BEFORE";
        testTrigger.eventType = "INSERT";
        testTrigger.triggerBody = "INSERT INTO " + testModifiableTable + " (`column`) VALUES('test_trigger_run');";

        mockMvc.perform(
                post("/trigger/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testTrigger))
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }
}
