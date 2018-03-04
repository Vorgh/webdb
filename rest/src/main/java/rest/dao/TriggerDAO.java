package rest.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import rest.mapper.TriggerMapper;
import rest.model.connection.UserConnection;
import rest.model.database.*;

import java.util.List;

@Transactional
public class TriggerDAO extends AbstractDatabaseDAO
{
    private static final Logger logger = LoggerFactory.getLogger(TriggerDAO.class);

    public TriggerDAO(UserConnection connection)
    {
        super(connection);
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
        return jdbcTemplate.query(
                "SELECT trigger_schema, trigger_name, event_manipulation, event_object_schema, event_object_table, action_statement, action_timing, created " +
                        "FROM information_schema.triggers " +
                        "WHERE trigger_schema=? AND trigger_name=?;",
                new Object[]{schemaName, triggerName},
                new TriggerMapper())
                .get(0);
    }

    public void createTrigger(String schemaName, Trigger requestTrigger)
    {
        String query = createTriggerQuery(requestTrigger);

        logger.info("Query executed: {}", query);
        jdbcTemplate.execute(query);
    }

    public void modifyTrigger(String schemaName, Trigger requestTrigger)
    {
        StringBuilder query = new StringBuilder();
        query.append("DROP TRIGGER IF EXISTS").append(quote(requestTrigger.schema)).append(".").append(requestTrigger.name).append(";\n");
        query.append(createTriggerQuery(requestTrigger));

        logger.info("Query executed: {}", query.toString());
        jdbcTemplate.execute(query.toString());
    }

    public void dropTrigger(String schemaName, String triggerName)
    {
        String query = "DROP TRIGGER IF EXISTS " + quote(schemaName) + "." + quote(triggerName) + ";";
        logger.info("Query executed: {}", query);
        jdbcTemplate.execute(query);
    }

    private String createTriggerQuery(Trigger requestTrigger)
    {
        StringBuilder query = new StringBuilder();
        String delimiter = ";";
        String tempDelimiter = "$$";

        if (requestTrigger.triggerBody.toUpperCase().startsWith("BEGIN") && requestTrigger.triggerBody.toUpperCase().endsWith("END"))
        {
            requestTrigger.triggerBody = requestTrigger.triggerBody + ";";
        }

        if (!(requestTrigger.triggerBody.toUpperCase().startsWith("BEGIN") && requestTrigger.triggerBody.toUpperCase().endsWith("END;")))
        {
            requestTrigger.triggerBody = "BEGIN\n" + requestTrigger.triggerBody + "\nEND;";
        }

        query.append("DELIMITER ").append(tempDelimiter).append("\n");
        query.append("CREATE TRIGGER ").append(quote(requestTrigger.schema)).append(".").append(quote(requestTrigger.name)).append(" ");
        query.append(requestTrigger.timing).append(" ").append(requestTrigger.eventType).append(" \n");
        query.append("ON ").append(quote(requestTrigger.eventSchema)).append(".").append(quote(requestTrigger.eventTable)).append(" \n");
        query.append("FOR EACH ROW \n");
        query.append(requestTrigger.triggerBody).append("\n");
        query.replace(query.lastIndexOf(delimiter), query.lastIndexOf(delimiter)+1, tempDelimiter);
        query.append("DELIMITER ").append(delimiter).append("\n");

        return query.toString();
    }
}

