package rest.model.database;

import java.util.Objects;

public class Column
{
    private String tableSchema;
    private String tableName;
    private String name;
    private int position;
    private String defaultValue;
    private boolean isNullable;
    private boolean isUnique;
    private String dataType;
    private int maxCharLength;
    private int octetLength;
    private int numericPrecision;
    private int numericScale;
    private int datePrecision;
    private String charSet;
    private String columnType;
    private boolean primaryKey;
    private boolean autoIncrement;

    @Override
    public String toString()
    {
        return "Column{" +
                "tableSchema='" + tableSchema + '\'' +
                ", tableName='" + tableName + '\'' +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", defaultValue='" + defaultValue + '\'' +
                ", isNullable=" + isNullable +
                ", isUnique=" + isUnique +
                ", dataType='" + dataType + '\'' +
                ", maxCharLength=" + maxCharLength +
                ", octetLength=" + octetLength +
                ", numericPrecision=" + numericPrecision +
                ", numericScale=" + numericScale +
                ", datePrecision=" + datePrecision +
                ", charSet='" + charSet + '\'' +
                ", columnType='" + columnType + '\'' +
                ", primaryKey=" + primaryKey +
                ", autoIncrement=" + autoIncrement +
                '}';
    }

    public String getTableSchema()
    {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema)
    {
        this.tableSchema = tableSchema;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public boolean isNullable()
    {
        return isNullable;
    }

    public void setNullable(boolean nullable)
    {
        isNullable = nullable;
    }

    public String getDataType()
    {
        return dataType;
    }

    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    public int getMaxCharLength()
    {
        return maxCharLength;
    }

    public void setMaxCharLength(int maxCharLength)
    {
        this.maxCharLength = maxCharLength;
    }

    public int getOctetLength()
    {
        return octetLength;
    }

    public void setOctetLength(int octetLength)
    {
        this.octetLength = octetLength;
    }

    public int getNumericPrecision()
    {
        return numericPrecision;
    }

    public void setNumericPrecision(int numericPrecision)
    {
        this.numericPrecision = numericPrecision;
    }

    public int getNumericScale()
    {
        return numericScale;
    }

    public void setNumericScale(int numericScale)
    {
        this.numericScale = numericScale;
    }

    public int getDatePrecision()
    {
        return datePrecision;
    }

    public void setDatePrecision(int datePrecision)
    {
        this.datePrecision = datePrecision;
    }

    public String getCharSet()
    {
        return charSet;
    }

    public void setCharSet(String charSet)
    {
        this.charSet = charSet;
    }

    public String getColumnType()
    {
        return columnType;
    }

    public void setColumnType(String columnType)
    {
        this.columnType = columnType;
    }

    public boolean isUnique()
    {
        return isUnique;
    }

    public void setUnique(boolean unique)
    {
        isUnique = unique;
    }

    public boolean isPrimaryKey()
    {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey)
    {
        this.primaryKey = primaryKey;
    }

    public boolean isAutoIncrement()
    {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement)
    {
        this.autoIncrement = autoIncrement;
    }
}
