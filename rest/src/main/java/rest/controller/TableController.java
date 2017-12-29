package rest.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rest.model.connection.UserConnection;
import rest.model.database.Column;
import rest.model.database.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rest.service.TableService;

@RestController
@RequestMapping("table")
public class TableController
{
    @Autowired
    private TableService tableService;

    @GetMapping("all")
    public ResponseEntity<List<Table>> getAllTablesMetadata(@RequestParam(value = "view", required = false) boolean isView,
                                                            @RequestParam("schema") String schema,
                                                            @AuthenticationPrincipal UserConnection connection)
    {
        List<Table> list;
        list = tableService.getAllTablesMetadata(schema, isView, connection);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("{tableName}/all")
    public ResponseEntity<List<Column>> getAllColumnsMetadata(@RequestParam("schema") String schema,
                                                              @PathVariable String tableName,
                                                              @AuthenticationPrincipal UserConnection connection)
    {
        List<Column> list;
        list = tableService.getAllColumnsMetadata(schema, tableName, connection);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /*@GetMapping("{name}/metadata")
    public ResponseEntity<Table> getTableMetadata(@RequestParam(value = "view", required = false) boolean view,
                                                        @PathVariable String name)
    {
        Table table;
        if (!view)
            list = databaseService.getAllTables(false, false);
        else
            list = databaseService.getAllTables(false, true);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }*/
}