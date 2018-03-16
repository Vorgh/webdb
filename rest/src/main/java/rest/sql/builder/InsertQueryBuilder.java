package rest.sql.builder;

import rest.sql.util.SqlStringUtils;

import static rest.sql.util.SqlStringUtils.bquote;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InsertQueryBuilder
{
    private String to;
    private List<String> columns;
    private List<String> values;

    public InsertQueryBuilder()
    {
        columns = new ArrayList<>();
        values = new ArrayList<>();
    }

    public InsertQueryBuilder insertInto(String table)
    {
        this.to = bquote(table);

        return this;
    }

    public InsertQueryBuilder insertInto(String schema, String table)
    {
        this.to = bquote(schema) + "." + bquote(table);

        return this;
    }

    public InsertQueryBuilder columns(List<String> columns)
    {
        this.columns = columns;

        return this;
    }

    public InsertQueryBuilder values(List<String> values)
    {
        this.values = values.stream().map(SqlStringUtils::quote).collect(Collectors.toList());

        return this;
    }

    public String build()
    {
        List<String> finalString = new ArrayList<>();

        if (!"".equals(to))
        {
            finalString.add("INSERT INTO " + to);
        }

        if (!columns.isEmpty())
        {
            finalString.add("(" + String.join(",", columns) + ")");
        }

        if (!values.isEmpty())
        {
            finalString.add("VALUES (" + String.join(",", values)+ ")");
        }

        return String.join(" ", finalString) + "; ";
    }
}
