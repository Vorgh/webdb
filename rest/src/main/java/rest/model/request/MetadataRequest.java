package rest.model.request;

public class MetadataRequest
{
    private String schemaName;
    private String tableName;
    private String[] columnNames;

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
}
