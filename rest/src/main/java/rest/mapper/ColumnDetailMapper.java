package rest.mapper;

import org.springframework.jdbc.core.RowMapper;
import rest.model.database.Column;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ColumnDetailMapper implements RowMapper<Column>
{
    @Override
    public Column mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Column column = new Column();
        column.setTableSchema(rs.getString("table_schema"));
        column.setTableName(rs.getString("table_name"));
        column.setName(rs.getString("column_name"));
        column.setPosition(rs.getInt("ordinal_position"));
        column.setDefaultValue(rs.getString("column_default"));
        column.setNullable(rs.getBoolean("is_nullable"));
        column.setDataType(rs.getString("data_type"));
        column.setMaxCharLength(rs.getInt("character_maximum_length"));
        column.setOctetLength(rs.getInt("character_octet_length"));
        column.setNumericPrecision(rs.getInt("numeric_precision"));
        column.setNumericScale(rs.getInt("numeric_scale"));
        column.setDatePrecision(rs.getInt("datetime_precision"));
        column.setCharSet(rs.getString("character_set_name"));
        column.setColumnType(rs.getString("column_type"));
        column.setKey(rs.getString("column_key"));
        column.setExtra(rs.getString("extra"));

        return column;
    }
}
