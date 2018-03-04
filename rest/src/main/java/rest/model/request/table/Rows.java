package rest.model.request.table;

import java.util.HashMap;
import java.util.List;

public class Rows
{
    private List<HashMap<String, String>> rows;

    public List<HashMap<String, String>> getRows()
    {
        return rows;
    }

    public void setRows(List<HashMap<String, String>> rows)
    {
        this.rows = rows;
    }
}
