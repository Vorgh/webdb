package rest.model.request.table.create;

import rest.model.database.Column;
import rest.model.database.Constraint;

import java.util.List;

public class CreateTableRequest
{
    public String tableName;
    public List<Column> columns;
    public List<Constraint> foreignKeys;
}
