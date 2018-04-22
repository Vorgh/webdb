package rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rest.model.connection.UserConnection;
import rest.model.database.Procedure;
import rest.model.request.Change;
import rest.service.ProcedureService;

import java.util.List;

@RestController
@RequestMapping("procedure")
public class ProcedureController
{
    @Autowired
    private ProcedureService procedureService;

    @GetMapping("all")
    public ResponseEntity<List<Procedure>> getAllProcedures(@RequestParam String schema,
                                                            @AuthenticationPrincipal UserConnection connection)
    {
        List<Procedure> list;
        list = procedureService.getAllProcedures(schema, connection);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("single")
    public ResponseEntity<Procedure> getProcedure(@RequestParam String schema,
                                                  @RequestParam String procedure,
                                                  @RequestParam(name="isFunction", required=false) String isFunction,
                                                  @AuthenticationPrincipal UserConnection connection)
    {
        Procedure procedureMetadata;
        procedureMetadata = procedureService.getProcedure(schema, procedure, isFunction, connection);

        return new ResponseEntity<>(procedureMetadata, HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<Void> createProcedure(@RequestBody Procedure requestProcedure,
                                                @AuthenticationPrincipal UserConnection connection)
    {
        procedureService.createProcedure(requestProcedure, connection);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("modify")
    public ResponseEntity<Void> modifyProcedure(@RequestBody Change<Procedure> change,
                                                @AuthenticationPrincipal UserConnection connection)
    {
        procedureService.modifyProcedure(change, connection);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("drop")
    public ResponseEntity<Void> deleteProcedure(@RequestParam String schema,
                                                @RequestParam String procedure,
                                                @RequestParam(name="isFunction", required=false) String isFunction,
                                                @AuthenticationPrincipal UserConnection connection)
    {
        procedureService.dropProcedure(schema, procedure, isFunction, connection);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}