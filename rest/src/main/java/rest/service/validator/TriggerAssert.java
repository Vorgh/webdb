package rest.service.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import rest.model.database.Trigger;

@Component
public class TriggerAssert
{
    public void assertParams(String schema, String name)
    {
        Assert.hasLength(schema, "Missing schema name.");
        Assert.hasLength(name, "Missing trigger name.");
    }

    public void assertParams(Trigger trigger)
    {
        Assert.hasLength(trigger.schema, "Missing schema name.");
        Assert.hasLength(trigger.name, "Missing trigger name.");
        Assert.hasLength(trigger.eventSchema, "Missing target schema name.");
        Assert.hasLength(trigger.eventTable, "Missing target table name.");
        Assert.hasLength(trigger.timing, "Missing trigger timing.");
        Assert.hasLength(trigger.eventType, "Missing event type.");
        Assert.hasLength(trigger.triggerBody, "Missing body.");
    }
}
