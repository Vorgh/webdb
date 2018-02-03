package rest.mapper;

import org.springframework.jdbc.core.RowMapper;
import rest.model.database.Constraint;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConstraintMapper implements RowMapper<Constraint>
{
    @Override
    public Constraint mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Constraint constraint = new Constraint();
        constraint.constraintName = rs.getString("constraint_name");
        constraint.schema = rs.getString("table_schema");
        constraint.table = rs.getString("table_name");
        constraint.column = rs.getString("column_name");
        constraint.refSchema = rs.getString("referenced_table_schema");
        constraint.refTable = rs.getString("referenced_table_name");
        constraint.refColumn = rs.getString("referenced_column_name");
        constraint.updateRule = rs.getString("update_rule");
        constraint.deleteRule = rs.getString("delete_rule");

        return constraint;
    }
}
