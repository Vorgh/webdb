package rest.sql.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
public class BatchExecutor
{
    private static final Logger logger = LoggerFactory.getLogger(BatchExecutor.class);

    private JdbcTemplate jdbcTemplate;

    public BatchExecutor(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<List<Map<String, Object>>> batch(String[] statements)
    {
        List<List<Map<String, Object>>> results = new ArrayList<>();

        logger.info("Batch execution started!");
        for (String s : statements)
        {
            if (s.toUpperCase().trim().startsWith("SELECT"))
            {
                results.add(jdbcTemplate.queryForList(s));
            }
            else
            {
                jdbcTemplate.execute(s);
            }
            logger.info("Statement executed: {}", s);
        }
        logger.info("Batch execution finished!");

        return results;
    }

    public void batchInsert(String[] insertQueries)
    {
        jdbcTemplate.batchUpdate(insertQueries);
        logger.info("Query executed: {}", String.join(",", insertQueries));
    }

    public void batchUpdate(String[] updateQueries)
    {
        jdbcTemplate.batchUpdate(updateQueries);
        logger.info("Query executed: {}", String.join(",", updateQueries));
    }

    public void batchDelete(String[] deleteQueries)
    {
        jdbcTemplate.batchUpdate(deleteQueries);
        logger.info("Query executed: {}", String.join(",", deleteQueries));
    }
}
