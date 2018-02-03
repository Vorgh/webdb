package rest.model.request.table.alter;

import rest.model.database.Column;
import rest.model.database.Constraint;
import rest.model.database.Index;

import java.util.*;

public class TableFormData
{
    private String tableName;
    private List<Column> columns;
    private List<Constraint> foreignKeys;
    private List<Index> indexes;

    @Override
    public String toString()
    {
        return "TableFormData{" +
                "tableName='" + tableName + '\'' +
                ", columns=" + columns.toString() +
                ", foreignKeys=" + foreignKeys.toString() +
                ", indexes=" + indexes.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableFormData that = (TableFormData) o;

        columns.sort(Comparator.comparing(Column::getName));
        that.columns.sort(Comparator.comparing(Column::getName));
        foreignKeys.sort((o1, o2) -> o1.constraintName.compareTo(o2.constraintName));
        that.foreignKeys.sort((o1, o2) -> o1.constraintName.compareTo(o2.constraintName));
        indexes.sort((o1, o2) -> o1.indexName.compareTo(o2.indexName));
        that.indexes.sort((o1, o2) -> o1.indexName.compareTo(o2.indexName));

        return Objects.equals(tableName, that.tableName) &&
                Objects.equals(columns, that.columns) &&
                Objects.equals(foreignKeys, that.foreignKeys) &&
                Objects.equals(indexes, that.indexes);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(tableName, columns, foreignKeys, indexes);
    }

    public String getTableName()
    {
        return tableName;
    }

    public List<Column> getColumns()
    {
        return columns;
    }

    public List<Constraint> getForeignKeys()
    {
        return foreignKeys;
    }

    public List<Index> getIndexes()
    {
        return indexes;
    }
}
