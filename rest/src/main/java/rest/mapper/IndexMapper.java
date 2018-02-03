package rest.mapper;

import org.springframework.jdbc.core.RowMapper;
import rest.model.database.Index;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IndexMapper implements RowMapper<Index>
{
    @Override
    public Index mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Index index = new Index();
        index.indexSchema = rs.getString("index_schema");
        index.indexName = rs.getString("index_name");
        index.schema = rs.getString("table_schema");
        index.table = rs.getString("table_name");
        index.column = rs.getString("column_name");
        index.unique = rs.getInt("non_unique") == 0;
        index.nullable = rs.getString("nullable").equals("YES");

        return index;
    }
}
