package rest.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import rest.model.database.Table;

public class TableMapper implements RowMapper<Table>
{
    @Override
    public Table mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Table table = new Table();
        table.setName(rs.getString("table_name"));

        return table;
    }
}