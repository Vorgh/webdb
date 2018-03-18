package rest.mapper;

import org.springframework.jdbc.core.RowMapper;
import rest.model.database.Parameter;
import rest.model.database.Procedure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProcedureMapper implements RowMapper<Procedure>
{
    private Map<String, List<Parameter>> paramMap;

    public ProcedureMapper(List<Parameter> paramList)
    {
        this.paramMap = paramList.stream()
                .collect(Collectors.groupingBy(Parameter::getProcedureIdentifier, Collectors.toCollection(ArrayList::new)));
    }

    @Override
    public Procedure mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Procedure procedure = new Procedure();
        procedure.setSchema(rs.getString("routine_schema"));
        procedure.setName(rs.getString("routine_name"));
        procedure.setType(rs.getString("routine_type"));
        procedure.setReturnType(rs.getString("dtd_identifier"));
        procedure.setBody(rs.getString("routine_definition"));
        procedure.setModified(rs.getDate("last_altered"));

        String paramKey = procedure.getIdentifier();
        procedure.setParamList(paramMap.get(paramKey));

        return procedure;
    }
}