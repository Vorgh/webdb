package rest.model.request.table.alter;

public class Change<T>
{
    public T from;
    public T to;

    @Override
    public String toString()
    {
        return "Change{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
