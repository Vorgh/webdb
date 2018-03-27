package rest.dao;

import rest.mapper.TriggerMapper;
import rest.model.connection.UserConnection;
import rest.model.database.*;
import rest.model.request.Change;
import rest.sql.executor.TriggerQueryExecutor;

import java.util.List;

public class TriggerDAO extends AbstractDatabaseDAO
{
    private TriggerQueryExecutor queryExecutor;

    public TriggerDAO(UserConnection connection)
    {
        super(connection);
        this.queryExecutor = new TriggerQueryExecutor(connection.getJdbcTemplate());
    }

    public List<Trigger> getAllTriggers(String schemaName)
    {
        return jdbcTemplate.query(
                "SELECT trigger_schema, trigger_name, event_manipulation, event_object_schema, event_object_table, action_statement, action_timing, created " +
                        "FROM information_schema.triggers " +
                        "WHERE trigger_schema=?;",
                new Object[]{schemaName},
                new TriggerMapper());
    }

    public Trigger getTrigger(String schemaName, String triggerName)
    {
        return jdbcTemplate.queryForObject(
                "SELECT trigger_schema, trigger_name, event_manipulation, event_object_schema, event_object_table, action_statement, action_timing, created " +
                        "FROM information_schema.triggers " +
                        "WHERE trigger_schema=? AND trigger_name=?;",
                new Object[]{schemaName, triggerName},
                new TriggerMapper());
    }

    public void createTrigger(Trigger requestTrigger)
    {
        queryExecutor.create(requestTrigger);
    }

    public void modifyTrigger(Change<Trigger> request)
    {
        queryExecutor.modify(request);
    }

    public void dropTrigger(String schemaName, String triggerName)
    {
        queryExecutor.delete(schemaName, triggerName);
    }
}

