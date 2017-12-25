package rest.mapper;

import org.springframework.jdbc.core.RowMapper;
import rest.model.database.Table;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TableDetailMapper extends TableMapper
{
    @Override
    public Table mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Table table = new Table();
        table.setSchema(rs.getString("table_schema"));
        table.setName(rs.getString("table_name"));
        table.setType(rs.getString("table_type"));
        table.setEngine(rs.getString("engine"));
        table.setCreationDate(rs.getDate("create_time"));
        table.setCollation(rs.getString("table_collation"));

        return table;
    }
}
