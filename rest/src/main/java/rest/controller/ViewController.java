package rest.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rest.model.connection.UserConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rest.model.database.Table;
import rest.model.database.View;
import rest.model.request.Change;
import rest.service.ViewService;

import java.util.List;

@RestController
@RequestMapping("view")
public class ViewController
{
    @Autowired
    private ViewService viewService;

    @GetMapping("metadata/all")
    public ResponseEntity<List<View>> getAllViewsMetadata(@RequestParam String schema,
                                                          @AuthenticationPrincipal UserConnection connection)
    {
        List<View> list;
        list = viewService.getAllViewsMetadata(schema, connection);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("metadata/single")
    public ResponseEntity<View> getViewMetadata(@RequestParam String schema,
                                                 @RequestParam String view,
                                                 @AuthenticationPrincipal UserConnection connection)
    {
        View viewMetadata;
        viewMetadata = viewService.getViewMetadata(schema, view, connection);

        return new ResponseEntity<>(viewMetadata, HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<Void> createView(@RequestBody View view,
                                           @AuthenticationPrincipal UserConnection connection)
    {
        viewService.createView(view, connection);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("alter")
    public ResponseEntity<Void> alterView(@RequestBody Change<View> request,
                                          @AuthenticationPrincipal UserConnection connection)
    {
        System.out.println(request);
        viewService.alterView(request, connection);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("drop")
    public ResponseEntity<Void> deleteView(@RequestParam String schema,
                                            @RequestParam String view,
                                            @AuthenticationPrincipal UserConnection connection)
    {
        viewService.dropView(schema, view, connection);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}