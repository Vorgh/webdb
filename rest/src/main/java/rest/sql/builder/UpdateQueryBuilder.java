package rest.sql.builder;

import rest.sql.model.UpdateSet;
import rest.sql.model.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static rest.sql.util.SqlStringUtils.bquote;

public class UpdateQueryBuilder
{
    private String target;
    private UpdateSet set;
    private Where where;

    public UpdateQueryBuilder()
    {
        set = new UpdateSet();
        where = new Where();
    }

    public UpdateQueryBuilder update(String table)
    {
        this.target = bquote(table);

        return this;
    }

    public UpdateQueryBuilder update(String schema, String table)
    {
        this.target = bquote(schema) + "." + bquote(table);

        return this;
    }

    public UpdateQueryBuilder set(String field, String value)
    {
        set.addCondition(field, value);

        return this;
    }

    public UpdateQueryBuilder set(Map<String, String> map)
    {
        set.addConditions(map);

        return this;
    }

    public UpdateQueryBuilder where(String left, String right)
    {
        where.addCondition(left, right);

        return this;
    }

    public UpdateQueryBuilder where(Map<String, String> whereMap)
    {
        where.addConditions(whereMap);

        return this;
    }

    public String build()
    {
        List<String> finalString = new ArrayList<>();

        if (!"".equals(target))
        {
            finalString.add("UPDATE " + target);
        }

        if (!set.isEmpty())
        {
            finalString.add("SET " + String.join(", ", set.toString()));
        }

        if (!where.isEmpty())
        {
            finalString.add("WHERE " + where.toString());
        }

        return String.join(" ", finalString) + "; ";
    }
}
