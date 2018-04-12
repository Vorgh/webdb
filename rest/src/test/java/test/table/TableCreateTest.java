package test.table;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.http.MediaType;
import rest.model.database.Column;
import rest.model.database.Table;
import rest.model.request.table.CreateTableRequest;
import test.base.TestAuthenticationBase;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TableCreateTest extends TestAuthenticationBase
{
    @Test
    public void whenCreatingTable_andSuccessful_thenNoContent_thenItShouldBeInTheDatabase() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        CreateTableRequest tableRequest = testCreateTableRequest();
        String testCreateTableJson = mapper.writeValueAsString(tableRequest);

        mockMvc.perform(
                post("/table/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testCreateTableJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());

        mockMvc.perform(
                get("/table/metadata/single")
                        .param("schema", tableRequest.schemaName)
                        .param("table", tableRequest.tableName)
                        .headers(getAuthHeaders()))
                .andExpect(status().isOk());

        mockMvc.perform(
                delete("/table/drop")
                        .param("schema", tableRequest.schemaName)
                        .param("table", tableRequest.tableName)
                        .headers(getAuthHeaders()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenCreatingTable_andInvalidParams_thenBadRequest() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        CreateTableRequest tableRequest = testCreateTableRequest();
        tableRequest.tableName = null;
        String testCreateTableJson = mapper.writeValueAsString(tableRequest);

        mockMvc.perform(
                post("/table/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(testCreateTableJson)
                        .headers(getAuthHeaders()))
                .andExpect(status().isBadRequest());
    }

    private CreateTableRequest testCreateTableRequest()
    {
        String testSchemaName = "teszt_schema";
        String testTableName = "test_create_table";

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

        return testRequest;
    }
}
