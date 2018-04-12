package rest.sql.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import rest.model.database.Trigger;
import rest.model.request.Change;

import static rest.sql.util.SqlStringUtils.bquote;

@Transactional
public class TriggerQueryExecutor implements BaseExecutor<Trigger>
{
    private static final Logger logger = LoggerFactory.getLogger(TriggerQueryExecutor.class);

    private JdbcTemplate jdbcTemplate;

    public TriggerQueryExecutor(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Trigger obj)
    {
        String sql = createTriggerQuery(obj);

        logger.info("Query executed: {}", sql);
        jdbcTemplate.execute(sql);
    }

    @Override
    public void modify(Change<Trigger> change)
    {
        delete(change.from.schema, change.from.name);

        try
        {
            create(change.to);
        }
        catch (DataAccessException e)
        {
            create(change.from);
            throw e;
        }
    }

    @Override
    public void delete(String schema, String name)
    {
        String sql = "DROP TRIGGER IF EXISTS " + bquote(schema) + "." + bquote(name) + ";";

        logger.info("Query executed: {}", sql);
        jdbcTemplate.execute(sql);
    }

    private String createTriggerQuery(Trigger requestTrigger)
    {
        StringBuilder query = new StringBuilder();
        //String delimiter = ";";
        //String tempDelimiter = "$$";

        //query.append("DELIMITER ").append(tempDelimiter).append("\n");
        query.append("CREATE TRIGGER ").append(bquote(requestTrigger.schema)).append(".").append(bquote(requestTrigger.name)).append(" ");
        query.append(requestTrigger.timing).append(" ").append(requestTrigger.eventType).append(" \n");
        query.append("ON ").append(bquote(requestTrigger.eventSchema)).append(".").append(bquote(requestTrigger.eventTable)).append(" \n");
        query.append("FOR EACH ROW \n");
        query.append(requestTrigger.triggerBody);
        //query.replace(query.lastIndexOf(delimiter), query.lastIndexOf(delimiter)+1, tempDelimiter);
        //query.append("DELIMITER ").append(delimiter).append("\n");

        return query.toString();
    }
}
