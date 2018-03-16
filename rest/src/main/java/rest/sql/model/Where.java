package rest.sql.model;

import rest.sql.util.SqlStringUtils;
import static rest.sql.util.SqlStringUtils.quote;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Where
{
    private List<String> left;
    private List<String> right;

    public Where()
    {
        this.left = new ArrayList<>();
        this.right = new ArrayList<>();
    }

    public Where(String left, String right)
    {
        this.left = new ArrayList<>();
        this.right = new ArrayList<>();
        this.left.add(left);
        this.right.add(quote(right));
    }

    public Where(Map<String, String> conditions)
    {
        this.left = new ArrayList<>(conditions.keySet());
        this.right = conditions.values().stream().map(SqlStringUtils::quote).collect(Collectors.toList());
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
            if (right.get(i) == null)
            {
                conditions.add(left.get(i) + " IS NULL");
            }
            else
            {
                conditions.add(left.get(i) + "=" + right.get(i));
            }
        }

        return String.join(" AND ", conditions);
    }
}
