package rest.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import rest.model.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rest.model.UserConnection;
import rest.service.DatabaseService;

@RestController
@RequestMapping("db")
public class DatabaseController
{
    @Autowired
    private DatabaseService databaseService;

    @GetMapping("tables")
    public ResponseEntity<List<Table>> getAllTables()
    {
        List<Table> list = databaseService.getAllTables();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}