package rest.model.database;

public class Parameter
{
    private String schema;
    private String procedureName;
    private String mode;
    private String name;
    private String type;

    public String getSchema()
    {
        return schema;
    }

    public void setSchema(String schema)
    {
        this.schema = schema;
    }

    public String getProcedureName()
    {
        return procedureName;
    }

    public void setProcedureName(String procedureName)
    {
        this.procedureName = procedureName;
    }

    public String getMode()
    {
        return mode;
    }

    public void setMode(String mode)
    {
        this.mode = mode;
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

    /**
     * Returns ('schema'.'name') of this parameter's procedure, which is always an unique identifier.
     * @return an unique identifier for this parameter's procedure
     */
    public String getProcedureIdentifier()
    {
        return schema + "." + procedureName;
    }

    @Override
    public String toString()
    {
        return "Parameter{" +
                "schema='" + schema + '\'' +
                ", procedureName='" + procedureName + '\'' +
                ", mode='" + mode + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
