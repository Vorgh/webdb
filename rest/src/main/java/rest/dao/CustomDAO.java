package rest.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import rest.model.connection.UserConnection;
import rest.sql.executor.BatchExecutor;

import java.util.List;
import java.util.Map;

@Transactional
public class CustomDAO extends AbstractDatabaseDAO
{
    private static final Logger logger = LoggerFactory.getLogger(CustomDAO.class);

    public CustomDAO(UserConnection connection)
    {
        super(connection);
    }

    public List<Map<String, Object>> execute(String[] statements)
    {
        BatchExecutor batchExecutor = new BatchExecutor(jdbcTemplate);

        return batchExecutor.batch(statements);
    }
}
