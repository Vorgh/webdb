package rest.mapper;

import org.springframework.jdbc.core.RowMapper;
import rest.model.database.Parameter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParameterMapper implements RowMapper<Parameter>
{
    @Override
    public Parameter mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Parameter parameter = new Parameter();

        parameter.setSchema(rs.getString("specific_schema"));
        parameter.setProcedureName(rs.getString("specific_name"));
        parameter.setMode(rs.getString("parameter_mode"));
        parameter.setName(rs.getString("parameter_name"));
        parameter.setType(rs.getString("dtd_identifier"));

        return parameter;
    }
}
