package rest.model.request.table;

import rest.model.database.Column;
import rest.model.database.Constraint;

import java.util.List;

public class CreateTableRequest
{
    public String schemaName;
    public String tableName;
    public List<Column> columns;
    public List<Constraint> foreignKeys;
}
