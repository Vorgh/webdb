package rest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rest.model.connection.UserConnection;
import rest.model.database.Column;
import rest.model.database.Constraint;
import rest.model.database.Index;
import rest.model.database.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rest.model.request.table.alter.AlterTableRequest;
import rest.model.request.RowRequest;
import rest.service.TableService;

@RestController
@RequestMapping("table")
public class TableController
{
    @Autowired
    private TableService tableService;

    @GetMapping("metadata/all")
    public ResponseEntity<List<Table>> getAllTablesMetadata(@RequestParam(value = "view", required = false) boolean isView,
                                                            @RequestParam String schema,
                                                            @AuthenticationPrincipal UserConnection connection)
    {
        List<Table> list;
        list = tableService.getAllTablesMetadata(schema, isView, connection);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("metadata/single")
    public ResponseEntity<Table> getTableMetadata(@RequestParam(value = "view", required = false) boolean isView,
                                                         @RequestParam String schema,
                                                         @RequestParam String table,
                                                         @AuthenticationPrincipal UserConnection connection)
    {
        Table tableMetadata;
        tableMetadata = tableService.getTableMetadata(schema, table, isView, connection);

        return new ResponseEntity<>(tableMetadata, HttpStatus.OK);
    }

    @GetMapping("metadata/columns")
    public ResponseEntity<List<Column>> getAllColumnsMetadata(@RequestParam String schema,
                                                              @RequestParam String table,
                                                              @AuthenticationPrincipal UserConnection connection)
    {
        List<Column> list;
        list = tableService.getAllColumnsMetadata(schema, table, connection);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("foreign")
    public ResponseEntity<List<Constraint>> getForeignKeys(@RequestParam String schema,
                                                                @RequestParam String table,
                                                                @AuthenticationPrincipal UserConnection connection)
    {
        List<Constraint> list = tableService.getForeignKeys(schema, table, connection);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("indexes")
    public ResponseEntity<List<Index>> getTableIndexes(@RequestParam String schema,
                                                       @RequestParam String table,
                                                       @AuthenticationPrincipal UserConnection connection)
    {
        List<Index> list = tableService.getTableIndexes(schema, table, connection);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("rows")
    public ResponseEntity<List<Map<String, Object>>> getRows(@RequestParam String schema,
                                                             @RequestParam String table,
                                                             @RequestParam String column,
                                                             @AuthenticationPrincipal UserConnection connection)
    {
        List<Map<String, Object>> list = tableService.getRowData(schema, table, column, connection);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("rows")
    public ResponseEntity<List<Map<String, Object>>> getRows(@RequestBody RowRequest request,
                                                             @AuthenticationPrincipal UserConnection connection)
    {
        List<Map<String, Object>> list = tableService.getRowData(request.getSchemaName(), request.getTableName(), request.getColumnNames(), connection);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("alter")
    public ResponseEntity<Void> alterTable(@RequestParam String schema,
                                           @RequestParam String table,
                                           @RequestBody AlterTableRequest request,
                                           @AuthenticationPrincipal UserConnection connection)
    {
        tableService.alterTable(schema, table, request, connection);

        return new ResponseEntity<>(HttpStatus.OK);
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