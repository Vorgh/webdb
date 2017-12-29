package rest.mapper;

import org.springframework.jdbc.core.RowMapper;
import rest.model.database.Schema;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SchemaMapper implements RowMapper<Schema>
{
    @Override
    public Schema mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Schema schema = new Schema();
        schema.setName(rs.getString("schema_name"));

        return schema;
    }
}
