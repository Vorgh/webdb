package rest.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rest.model.connection.UserConnection;
import rest.model.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rest.model.request.Change;
import rest.service.TriggerService;

@RestController
@RequestMapping("trigger")
public class TriggerController
{
    private TriggerService triggerService;

    @Autowired
    public TriggerController(TriggerService triggerService)
    {
        this.triggerService = triggerService;
    }

    @GetMapping("all")
    public ResponseEntity<List<Trigger>> getAllTriggers(@RequestParam String schema,
                                                        @AuthenticationPrincipal UserConnection connection)
    {
        List<Trigger> list;
        list = triggerService.getAllTriggers(schema, connection);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("single")
    public ResponseEntity<Trigger> getTrigger(@RequestParam String schema,
                                              @RequestParam String trigger,
                                              @AuthenticationPrincipal UserConnection connection)
    {
        Trigger triggerMetadata;
        triggerMetadata = triggerService.getTrigger(schema, trigger, connection);

        return new ResponseEntity<>(triggerMetadata, HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<Void> createTrigger(@RequestBody Trigger requestTrigger,
                                              @AuthenticationPrincipal UserConnection connection)
    {
        triggerService.createTrigger(requestTrigger, connection);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("modify")
    public ResponseEntity<Void> modifyTrigger(@RequestBody Change<Trigger> request,
                                              @AuthenticationPrincipal UserConnection connection)
    {
        triggerService.modifyTrigger(request, connection);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("drop")
    public ResponseEntity<Void> deleteTrigger(@RequestParam String schema,
                                              @RequestParam String trigger,
                                              @AuthenticationPrincipal UserConnection connection)
    {
        triggerService.dropTrigger(schema, trigger, connection);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}