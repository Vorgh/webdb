package rest.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BatchExecuter
{
    private static final Logger logger = LoggerFactory.getLogger(BatchExecuter.class);

    private JdbcTemplate jdbcTemplate;

    public BatchExecuter(JdbcTemplate jdbcTemplate)
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
