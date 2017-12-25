package rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rest.model.database.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rest.service.DatabaseService;

@RestController
@RequestMapping("table")
public class DatabaseController
{
    @Autowired
    private DatabaseService databaseService;

    @GetMapping("all")
    public ResponseEntity<List<Table>> getAllTables(@RequestParam(value = "view", required = false) boolean view)
    {
        List<Table> list;
        if (!view)
            list = databaseService.getAllTables(false, false);
        else
            list = databaseService.getAllTables(false, true);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("allDetailed")
    public ResponseEntity<List<Table>> getAllTablesDetailed(@RequestParam(value = "view", required = false) boolean view)
    {
        List<Table> list;
        if (!view)
            list = databaseService.getAllTables(true, false);
        else
            list = databaseService.getAllTables(true, true);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}