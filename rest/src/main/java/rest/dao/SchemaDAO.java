package rest.dao;

import org.springframework.transaction.annotation.Transactional;
import rest.mapper.SchemaMapper;
import rest.model.connection.UserConnection;
import rest.model.database.Schema;

import java.util.List;

@Transactional
public class SchemaDAO extends AbstractDatabaseDAO
{
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
}
