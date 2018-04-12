package test.trigger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import rest.model.database.Trigger;
import rest.model.request.Change;
import rest.sql.util.SQLObjectBeginEndWrapper;
import test.base.TestAuthenticationBase;

import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TriggerModifyTest extends TestAuthenticationBase
{
    @Autowired
    private SQLObjectBeginEndWrapper beginEndWrapper;
    
    private static String testSchemaName = "teszt_schema";
    private static String testTriggerName = "modify_test_trigger";
    private static String testEventTableName = "trigger_test_table_1";
    private static String testModifiableTable = "trigger_test_table_2";
    private static Trigger testTrigger;

    @BeforeClass
    public static void createTestTrigger() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        testTrigger = new Trigger();
        testTrigger.schema = testSchemaName;
        testTrigger.name = testTriggerName;
        testTrigger.eventSchema = testSchemaName;
        testTrigger.eventTable = testEventTableName;
        testTrigger.timing = "BEFORE";
        testTrigger.eventType = "INSERT";
        testTrigger.triggerBody = "INSERT INTO " + testModifiableTable + "(`column`) VALUES ('test_trigger_run');";
        String testTriggerJson = mapper.writeValueAsString(testTrigger);
        
        mockMvc.perform(
                post("/trigger/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testTriggerJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenModifyingTrigger_andSuccessful_thenNoContent_thenItShouldChange() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        Change<Trigger> testTriggerChange = testTriggerChange(testTrigger);
        String testChangeJson = mapper.writeValueAsString(testTriggerChange);

        mockMvc.perform(
                put("/trigger/modify")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testChangeJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());

        mockMvc.perform(
                get("/trigger/single")
                        .param("schema", testTrigger.schema)
                        .param("trigger", testTrigger.name)
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.schema", equalToIgnoringCase(testTriggerChange.to.schema)))
                .andExpect(jsonPath("$.name", equalToIgnoringCase(testTriggerChange.to.name)))
                .andExpect(jsonPath("$.eventType", equalToIgnoringCase(testTriggerChange.to.eventType)))
                .andExpect(jsonPath("$.eventSchema", equalToIgnoringCase(testTriggerChange.to.eventSchema)))
                .andExpect(jsonPath("$.eventTable", equalToIgnoringCase(testTriggerChange.to.eventTable)))
                .andExpect(jsonPath("$.triggerBody", equalToIgnoringCase(beginEndWrapper.wrap(testTriggerChange.to.triggerBody))))
                .andExpect(status().isOk());
    }

    @Test
    public void whenModifyingTrigger_andInvalidSql_thenConflict_andShouldRollback() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();

        Change<Trigger> testTriggerChange = testTriggerChange(testTrigger);
        String originalBody = testTriggerChange.from.triggerBody;
        testTriggerChange.to.triggerBody = "lsdkgjsdpogkso";
        String testChangeJson = mapper.writeValueAsString(testTriggerChange);

        mockMvc.perform(
                put("/trigger/modify")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testChangeJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isConflict());

        mockMvc.perform(
                get("/trigger/single")
                        .param("schema", testTriggerChange.from.schema)
                        .param("trigger", testTriggerChange.from.name)
                        .headers(getAuthHeaders()))
                .andExpect(jsonPath("$.triggerBody", equalToIgnoringCase(originalBody.substring(0, originalBody.length()-1))))
                .andExpect(status().isOk());
    }

    @Test
    public void whenModifyingTrigger_andInvalidParams_thenBadRequest() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        Change<Trigger> testTriggerChange = testTriggerChange(testTrigger);
        testTriggerChange.to.schema = null;
        String testChangeJson = mapper.writeValueAsString(testTriggerChange);

        mockMvc.perform(
                put("/trigger/modify")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testChangeJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isBadRequest());
    }

    @AfterClass
    public static void deleteTestTrigger() throws Exception
    {
        mockMvc.perform(
                delete("/trigger/drop")
                        .param("schema", testTrigger.schema)
                        .param("trigger", testTrigger.name)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }

    private Change<Trigger> testTriggerChange(Trigger from)
    {
        Trigger testTrigger2 = new Trigger();
        testTrigger2.schema = testSchemaName;
        testTrigger2.name = testTriggerName;
        testTrigger2.eventSchema = testSchemaName;
        testTrigger2.eventTable = testEventTableName;
        testTrigger2.timing = "AFTER";
        testTrigger2.eventType = "UPDATE";
        testTrigger2.triggerBody = from.triggerBody;

        Change<Trigger> change = new Change<>();
        change.from = from;
        change.to = testTrigger2;

        return change;
    }
}
