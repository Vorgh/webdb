package rest.sql.builder;

import rest.sql.model.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static rest.sql.util.SqlStringUtils.bquote;

public class DeleteQueryBuilder
{
    private String from;
    private Where where;

    public DeleteQueryBuilder()
    {
        where = new Where();
    }

    public DeleteQueryBuilder from(String table)
    {
        this.from = bquote(table);

        return this;
    }

    public DeleteQueryBuilder from(String schema, String table)
    {
        this.from = bquote(schema) + "." + bquote(table);

        return this;
    }

    public DeleteQueryBuilder where(String left, String right)
    {
        where.addCondition(left, right);

        return this;
    }

    public DeleteQueryBuilder where(Map<String, String> map)
    {
        where.addConditions(map);

        return this;
    }

    public String build()
    {
        List<String> finalString = new ArrayList<>();

        if (!"".equals(from))
        {
            finalString.add("DELETE FROM " + from);
        }

        if (!where.isEmpty())
        {
            finalString.add("WHERE " + where.toString());
        }

        return String.join(" ", finalString) + "; ";
    }
}
