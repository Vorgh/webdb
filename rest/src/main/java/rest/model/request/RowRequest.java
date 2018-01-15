package rest.model.request;

public class RowRequest
{
    private String schemaName;
    private String tableName;
    private String[] columnNames;
    private String[] conditions;
    private String[] orderings;
    private String[] groupings;

    public String getSchemaName()
    {
        return schemaName;
    }

    public String getTableName()
    {
        return tableName;
    }

    public String[] getColumnNames()
    {
        return columnNames;
    }

    public String[] getConditions()
    {
        return conditions;
    }

    public String[] getOrderings()
    {
        return orderings;
    }

    public String[] getGroupings()
    {
        return groupings;
    }
}
