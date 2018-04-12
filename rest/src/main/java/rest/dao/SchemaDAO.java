package rest.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import rest.mapper.SchemaMapper;
import rest.model.connection.UserConnection;
import rest.model.database.Schema;

import java.util.List;

import static rest.sql.util.SqlStringUtils.bquote;

@Transactional
public class SchemaDAO extends AbstractDatabaseDAO
{
    private static final Logger logger = LoggerFactory.getLogger(SchemaDAO.class);

    public SchemaDAO(UserConnection connection)
    {
        super(connection);
    }

    public List<Schema> getAllSchemasMetadata()
    {
        return jdbcTemplate.query(
                "SELECT schema_name " +
                        "FROM information_schema.SCHEMATA " +
                        "WHERE SCHEMA_NAME not in ('information_schema', 'performance_schema', 'mysql', 'sys');",
                new SchemaMapper());
    }

    public void createSchema(String schemaName)
    {
        String query = "CREATE DATABASE " + bquote(schemaName) +
                " DEFAULT CHARACTER SET utf8 " +
                " DEFAULT COLLATE utf8_general_ci;";

        logger.info("Query executed: {}", query);
        jdbcTemplate.execute(query);
    }

    public void dropSchema(String schemaName)
    {
        String query = "DROP DATABASE " + bquote(schemaName) + ";";

        logger.info("Query executed: {}", query);
        jdbcTemplate.execute(query);
    }
}
