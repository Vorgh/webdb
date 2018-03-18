package rest.model.request.table;

import rest.model.request.Change;

import java.util.List;
import java.util.Map;

public class RowModifyRequest
{
    public List<Change<Map<String, Object>>> changes;

    @Override
    public String toString()
    {
        return "RowModifyRequest{" +
                "rowChanges=" + changes +
                '}';
    }
}
