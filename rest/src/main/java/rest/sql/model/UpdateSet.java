package rest.sql.model;

import rest.sql.util.SqlStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static rest.sql.util.SqlStringUtils.quote;

public class UpdateSet
{
    private List<String> left;
    private List<String> right;

    public UpdateSet()
    {
        this.left = new ArrayList<>();
        this.right = new ArrayList<>();
    }

    public UpdateSet(String left, String right)
    {
        this.left = new ArrayList<>();
        this.right = new ArrayList<>();
        this.left.add(left);
        this.right.add(quote(right));
    }

    public UpdateSet(Map<String, String> updates)
    {
        this.left = new ArrayList<>(updates.keySet());
        this.right = updates.values().stream().map(SqlStringUtils::quote).collect(Collectors.toList());
    }

    public void addCondition(String left, String right)
    {
        this.left.add(left);
        this.right.add(quote(right));
    }

    public void addConditions(Map<String, String> conditions)
    {
        this.left.addAll(conditions.keySet());
        this.right.addAll(conditions.values().stream().map(SqlStringUtils::quote).collect(Collectors.toList()));
    }

    public boolean isEmpty()
    {
        return this.left.isEmpty() || this.right.isEmpty();
    }

    @Override
    public String toString()
    {
        List<String> conditions = new ArrayList<>();

        for (int i=0; i<left.size(); i++)
        {
            conditions.add(left.get(i) + "=" + right.get(i));
        }

        return String.join(",", conditions);
    }
}
