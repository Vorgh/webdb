package rest.model.database;

import java.util.Date;
import java.util.List;

public class Procedure
{
    private String schema;
    private String name;
    private String type;
    private String returnType;
    private String body;
    private Date modified;
    private List<Parameter> paramList;

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

    public String getReturnType()
    {
        return returnType;
    }

    public void setReturnType(String dataType)
    {
        this.returnType = dataType;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public Date getModified()
    {
        return modified;
    }

    public void setModified(Date modified)
    {
        this.modified = modified;
    }

    public List<Parameter> getParamList()
    {
        return paramList;
    }

    public void setParamList(List<Parameter> paramList)
    {
        this.paramList = paramList;
    }

    /**
     * Returns ('schema'.'name') of this procedure, which is always an unique identifier.
     * @return an unique identifier for this procedure
     */
    public String getIdentifier()
    {
        return schema + "." + name;
    }

    @Override
    public String toString()
    {
        return "Procedure{" +
                "schema='" + schema + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", returnType='" + returnType + '\'' +
                ", body='" + body + '\'' +
                ", modified=" + modified +
                ", paramList=" + paramList +
                '}';
    }
}
