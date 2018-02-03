package rest.model.database;

public class Constraint
{
    public String constraintName;
    public String schema;
    public String table;
    public String column;
    public String refSchema;
    public String refTable;
    public String refColumn;
    public String updateRule;
    public String deleteRule;

    @Override
    public String toString()
    {
        return "Constraint{" +
                "constraintName='" + constraintName + '\'' +
                ", refSchema='" + refSchema + '\'' +
                ", refTable='" + refTable + '\'' +
                ", refColumn='" + refColumn + '\'' +
                ", updateRule='" + updateRule + '\'' +
                ", deleteRule='" + deleteRule + '\'' +
                '}';
    }
}
