package test.table;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import rest.model.database.Column;
import rest.model.database.Table;
import rest.model.request.table.CreateTableRequest;
import test.base.TestAuthenticationBase;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TableDeleteTest extends TestAuthenticationBase
{
    private String testSchemaName = "teszt_schema";
    private String testTableName = "test_table";

    @Test
    public void whenDeletingTable_andSuccessful_thenNoContent() throws Exception
    {
        createTestTable();
        mockMvc.perform(
                delete("/table/drop")
                        .param("schema", testSchemaName)
                        .param("table", testTableName)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenDeletingTable_andInvalidParams_thenBadRequest() throws Exception
    {
        mockMvc.perform(
                delete("/table/drop")
                        .param("schema", testSchemaName)
                        .headers(getAuthHeaders()))
                .andExpect(status().isBadRequest());
    }
    
    private void createTestTable() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        CreateTableRequest testRequest = new CreateTableRequest();
        testRequest.schemaName = testSchemaName;
        testRequest.tableName = testTableName;
        testRequest.columns = new ArrayList<>();
        Column testColumn = new Column();
        testColumn.setTableSchema("teszt_schema");
        testColumn.setTableName(testTableName);
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
}
