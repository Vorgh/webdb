package rest.model.database;

public class View
{
    private String schema;
    private String name;
    private String selectQuery;

    public String getSchema()
    {
        return schema;
    }

    public void setSchema(String schema)
    {
        this.schema = schema;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSelectQuery()
    {
        return selectQuery;
    }

    public void setSelectQuery(String selectQuery)
    {
        this.selectQuery = selectQuery;
    }
}
