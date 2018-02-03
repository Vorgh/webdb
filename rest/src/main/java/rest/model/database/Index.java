package rest.model.database;

public class Index
{
    public String indexSchema;
    public String indexName;
    public String schema;
    public String table;
    public String column;
    public boolean unique;
    public boolean nullable;

    @Override
    public String toString()
    {
        return "Index{" +
                "indexSchema='" + indexSchema + '\'' +
                ", indexName='" + indexName + '\'' +
                ", schema='" + schema + '\'' +
                ", table='" + table + '\'' +
                ", column='" + column + '\'' +
                ", unique=" + unique +
                ", nullable=" + nullable +
                '}';
    }
}
