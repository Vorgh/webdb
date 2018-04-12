package test.table;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import rest.model.database.Column;
import rest.model.database.Table;
import rest.model.request.Change;
import rest.model.request.table.AlterTableRequest;
import rest.model.request.table.CreateTableRequest;
import rest.sql.util.SQLObjectBeginEndWrapper;
import test.base.TestAuthenticationBase;

import java.util.ArrayList;

import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TableModifyTest extends TestAuthenticationBase
{
    private String testSchemaName = "teszt_schema";
    private String originalTestTableName = "test_alter_table";
    private String newTestTableName = "test_alter_table_changed";
    private String originalTestColumnName = "test_column";
    private String newTestColumnName = "test_column_changed";

    @Before
    public void createTestTable() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        CreateTableRequest testRequest = new CreateTableRequest();
        testRequest.schemaName = testSchemaName;
        testRequest.tableName = originalTestTableName;
        testRequest.columns = new ArrayList<>();
        Column testColumn = new Column();
        testColumn.setTableSchema(testSchemaName);
        testColumn.setTableName(originalTestTableName);
        testColumn.setName("test_column");
        testColumn.setColumnType("INT(10)");
        testRequest.columns.add(testColumn);
        testRequest.foreignKeys = new ArrayList<>();

        mockMvc.perform(
                post("/table/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testRequest))
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenModifyingTable_andSuccessful_thenNoContent_thenItShouldChange() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        AlterTableRequest testRequest = testModifyTableRequest();
        String testChangeJson = mapper.writeValueAsString(testRequest);

        mockMvc.perform(
                put("/table/alter")
                        .param("schema", testSchemaName)
                        .param("table", originalTestTableName)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testChangeJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());

        mockMvc.perform(
                get("/table/metadata/columns")
                        .param("schema", testSchemaName)
                        .param("table", newTestTableName)
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].tableSchema", equalToIgnoringCase(testSchemaName)))
                .andExpect(jsonPath("$.[0].tableName", equalToIgnoringCase(newTestTableName)))
                .andExpect(jsonPath("$.[0].name", equalToIgnoringCase(newTestColumnName)))
                .andExpect(jsonPath("$.[0].columnType", equalToIgnoringCase("INT(20)")))
                .andExpect(status().isOk());

        mockMvc.perform(
                delete("/table/drop")
                        .param("schema", testSchemaName)
                        .param("table", newTestTableName)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenModifyingTable_andInvalidSql_thenConflict_andShouldRollback() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        AlterTableRequest testRequest = testModifyTableRequest();
        testRequest.columnChange.get(0).to.setColumnType("wrong_type");
        String testChangeJson = mapper.writeValueAsString(testRequest);

        mockMvc.perform(
                put("/table/alter")
                        .param("schema", testSchemaName)
                        .param("table", originalTestTableName)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testChangeJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isConflict());

        mockMvc.perform(
                delete("/table/drop")
                        .param("schema", testSchemaName)
                        .param("table", originalTestTableName)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenModifyingTable_andInvalidParams_thenBadRequest() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        AlterTableRequest testRequest = testModifyTableRequest();
        testRequest.nameChange = "";
        String testChangeJson = mapper.writeValueAsString(testRequest);

        mockMvc.perform(
                put("/table/alter")
                        .param("schema", testSchemaName)
                        .param("table", originalTestTableName)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testChangeJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isBadRequest());

        mockMvc.perform(
                delete("/table/drop")
                        .param("schema", testSchemaName)
                        .param("table", originalTestTableName)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }

    private AlterTableRequest testModifyTableRequest()
    {
        AlterTableRequest testRequest = new AlterTableRequest();
        testRequest.nameChange = newTestTableName;
        testRequest.columnChange = new ArrayList<>();

        Change<Column> columnChange = new Change<>();
        Column testColumn1 = new Column();
        testColumn1.setTableSchema(testSchemaName);
        testColumn1.setTableName(originalTestTableName);
        testColumn1.setName(originalTestColumnName);
        testColumn1.setColumnType("INT(10)");
        columnChange.from = testColumn1;

        Column testColumn2 = new Column();
        testColumn2.setTableSchema(testSchemaName);
        testColumn2.setTableName(originalTestTableName);
        testColumn2.setName(newTestColumnName);
        testColumn2.setColumnType("INT(20)");
        columnChange.to = testColumn2;

        testRequest.columnChange.add(columnChange);

        return testRequest;
    }
}
