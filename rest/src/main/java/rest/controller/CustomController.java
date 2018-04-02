package rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rest.model.connection.UserConnection;
import rest.model.database.Procedure;
import rest.model.request.Change;
import rest.service.CustomService;
import rest.service.ProcedureService;

import java.util.List;
import java.util.Map;

@RestController
public class CustomController
{
    @Autowired
    private CustomService customService;

    @PostMapping("custom")
    public ResponseEntity<List<Map<String, Object>>> executeSql(@RequestBody String sql,
                                                      @AuthenticationPrincipal UserConnection connection)
    {
        List<Map<String, Object>> result = this.customService.execute(sql, connection);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}