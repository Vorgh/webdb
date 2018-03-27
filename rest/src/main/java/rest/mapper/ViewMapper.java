package rest.mapper;

import org.springframework.jdbc.core.RowMapper;
import rest.model.database.Table;
import rest.model.database.View;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewMapper implements RowMapper<View>
{
    @Override
    public View mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        View view = new View();
        view.setSchema(rs.getString("table_schema"));
        view.setName(rs.getString("table_name"));
        view.setSelectQuery(rs.getString("view_definition"));

        return view;
    }
}
