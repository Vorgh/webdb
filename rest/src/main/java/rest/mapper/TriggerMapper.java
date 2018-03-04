package rest.mapper;

import org.springframework.jdbc.core.RowMapper;
import rest.model.database.Schema;
import rest.model.database.Trigger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TriggerMapper implements RowMapper<Trigger>
{
    @Override
    public Trigger mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Trigger trigger = new Trigger();
        trigger.schema = rs.getString("trigger_schema");
        trigger.name = rs.getString("trigger_name");
        trigger.eventType = rs.getString("event_manipulation");
        trigger.eventSchema = rs.getString("event_object_schema");
        trigger.eventTable = rs.getString("event_object_table");
        trigger.triggerBody = rs.getString("action_statement");
        trigger.timing = rs.getString("action_timing");
        trigger.createdAt = rs.getDate("created");

        return trigger;
    }
}