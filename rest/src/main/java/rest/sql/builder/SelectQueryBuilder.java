package rest.sql.builder;

import rest.sql.model.Order;
import rest.sql.model.OrderType;
import rest.sql.model.Where;
import rest.sql.util.SqlStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectQueryBuilder
{
    private List<String> selectColumns;
    private String from;
    private Where whereConditions;
    private Order orders;

    public SelectQueryBuilder()
    {
        selectColumns = new ArrayList<>();
        whereConditions = new Where();
        orders = new Order();
    }

    public SelectQueryBuilder select(List<String> selects)
    {
        selectColumns.addAll(selects);

        return this;
    }

    public SelectQueryBuilder select(String select)
    {
        selectColumns.add(select);

        return this;
    }

    public SelectQueryBuilder from(String table)
    {
        from = SqlStringUtils.bquote(table);

        return this;
    }

    public SelectQueryBuilder from(String schema, String table)
    {
        from = SqlStringUtils.bquote(schema) + "." + SqlStringUtils.bquote(table);

        return this;
    }

    public SelectQueryBuilder where(String left, String right)
    {
        this.whereConditions.addCondition(left, right);

        return this;
    }

    public SelectQueryBuilder where(Map<String, String> conditions)
    {
        whereConditions.addConditions(conditions);

        return this;
    }

    public SelectQueryBuilder order(String field, OrderType type)
    {
        orders.add(field, type);

        return this;
    }

    public SelectQueryBuilder order(Map<String, OrderType> orderMap)
    {
        orders.addAll(orderMap);

        return this;
    }

    public String build()
    {
        List<String> finalString = new ArrayList<>();

        if (!selectColumns.isEmpty())
        {
            finalString.add("SELECT " + String.join(",", selectColumns));
        }

        if (!"".equals(from))
        {
            finalString.add("FROM " + from);
        }

        if (!whereConditions.isEmpty())
        {
            finalString.add("WHERE " + whereConditions.toString());
        }

        if (!orders.isEmpty())
        {
            finalString.add("ORDER BY " + orders.toString());
        }

        return String.join(" ", finalString) + "; ";
    }
}
