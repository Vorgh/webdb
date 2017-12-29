package rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rest.model.connection.UserConnection;
import rest.model.database.Schema;
import rest.service.SchemaService;

import java.util.List;

@RestController
@RequestMapping("schema")
public class SchemaController
{
    @Autowired
    private SchemaService schemaService;

    @GetMapping("all")
    public ResponseEntity<List<Schema>> getAllSchemaMetadata(@AuthenticationPrincipal UserConnection connection)
    {
        List<Schema> list = schemaService.getAllSchemasMetadata(connection);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
