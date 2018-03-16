package rest.sql.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order
{
    private Map<String, OrderType> orderMap;

    public Order()
    {
        this.orderMap = new HashMap<>();
    }

    public Order(String field, OrderType type)
    {
        this.orderMap = new HashMap<>();
        this.orderMap.put(field, type);
    }

    public Order(Map<String, OrderType> map)
    {
        this.orderMap = new HashMap<>();
        this.orderMap.putAll(map);
    }

    public void add(String field, OrderType type)
    {
        this.orderMap.put(field, type);
    }

    public void addAll(Map<String, OrderType> map)
    {
        this.orderMap.putAll(map);
    }

    public boolean isEmpty()
    {
        return orderMap.isEmpty();
    }

    @Override
    public String toString()
    {
        List<String> orders = new ArrayList<>();
        this.orderMap.forEach((key, value) -> orders.add(key + " " + value.toString()));

        return String.join(",", orders);
    }
}
