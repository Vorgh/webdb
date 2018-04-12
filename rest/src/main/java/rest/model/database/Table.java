package rest.model.database;

import java.sql.Date;

public class Table
{
    private String schema;
    private String name;
    private String type;
    private String engine;
    private Date creationDate;
    private String collation;

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

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getEngine()
    {
        return engine;
    }

    public void setEngine(String engine)
    {
        this.engine = engine;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public String getCollation()
    {
        return collation;
    }

    public void setCollation(String collation)
    {
        this.collation = collation;
    }
}
