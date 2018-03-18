package rest.sql.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BatchExecutor
{
    private static final Logger logger = LoggerFactory.getLogger(BatchExecutor.class);

    private JdbcTemplate jdbcTemplate;

    public BatchExecutor(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
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
