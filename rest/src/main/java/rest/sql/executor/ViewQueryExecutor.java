package rest.sql.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import rest.model.database.View;
import rest.model.request.Change;

import static rest.sql.util.SqlStringUtils.bquote;

public class ViewQueryExecutor implements BaseExecutor<View>
{
    private static final Logger logger = LoggerFactory.getLogger(ViewQueryExecutor.class);

    private JdbcTemplate jdbcTemplate;

    public ViewQueryExecutor(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(View view)
    {
        String query = "CREATE VIEW " + bquote(view.getSchema()) + "." + bquote(view.getName()) +
                " AS " + view.getSelectQuery() + ";";

        logger.info("Query executed: {}", query);
        jdbcTemplate.execute(query);
    }

    @Override
    public void modify(Change<View> change)
    {
        if (change.from.getName().equals(change.to.getName()))
        {
            String query = "ALTER VIEW " + bquote(change.to.getSchema()) + "." + bquote(change.to.getName()) +
                    " AS " + change.to.getSelectQuery() + ";";

            logger.info("Query executed: {}", query);
            jdbcTemplate.execute(query);
        }
        else
        {
            delete(change.from.getSchema(), change.from.getName());

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
    }

    @Override
    public void delete(String schema, String name)
    {
        String query = "DROP VIEW IF EXISTS " + bquote(schema) + "." + bquote(name) + ";";

        logger.info("Query executed: {}", query);
        jdbcTemplate.execute(query);
    }
}
