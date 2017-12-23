package rest.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import rest.model.Table;

public class TableMapper implements RowMapper<Table>
{
    public Table mapRow(ResultSet row, int rowNum) throws SQLException
    {
        Table table = new Table();
        table.setName(row.getString("Tables_in_webdb"));
        return table;
    }
}