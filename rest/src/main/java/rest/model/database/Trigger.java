package rest.model.database;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Trigger
{
    public String schema;
    public String name;
    public String eventType;
    public String eventSchema;
    public String eventTable;
    public String triggerBody;
    public String timing;
    public Date createdAt;
}
