package rest.dao;

import org.springframework.transaction.annotation.Transactional;
import rest.mapper.ColumnDetailMapper;
import rest.mapper.TableDetailMapper;
import rest.model.connection.UserConnection;
import rest.model.database.Column;
import rest.model.database.Table;

import java.util.List;

@Transactional
public class TableDAO extends AbstractDatabaseDAO
{
    public TableDAO(UserConnection connection)
    {
        super(connection);
    }

    public List<Table> getAllTablesMetadata(String schemaName)
    {
        return jdbcTemplate.query(
            "SELECT table_schema, table_name, table_type, engine, create_time, table_collation " +
                    "FROM information_schema.tables " +
                    "WHERE table_schema=? AND table_type='BASE TABLE';",
            new Object[] {schemaName},
            new TableDetailMapper());
    }

    public List<Table> getAllViewsMetadata(String schemaName)
    {
        return jdbcTemplate.query(
                "SELECT table_schema, table_name, table_type, engine, create_time, table_collation " +
                        "FROM information_schema.tables " +
                        "WHERE table_schema=? AND table_type='VIEW';",
                new Object[] {schemaName},
                new TableDetailMapper());
    }

    public List<Column> getAllColumnsMetadata(String schemaName, String tableName)
    {
        return jdbcTemplate.query(
                "SELECT table_schema, table_name, column_name, ordinal_position, column_default, is_nullable, data_type, character_maximum_length, character_octet_length, " +
                        "numeric_precision, numeric_scale, datetime_precision, character_set_name, column_type, column_key, extra " +
                "FROM information_schema.columns " +
                "WHERE table_schema=? AND table_name=? " +
                "ORDER BY ordinal_position ASC;",
                new Object[] {schemaName, tableName},
                new ColumnDetailMapper());
    }
}
