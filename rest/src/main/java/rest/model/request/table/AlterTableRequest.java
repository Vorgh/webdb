package rest.model.request.table;

import rest.model.database.Column;
import rest.model.database.Constraint;
import rest.model.database.Index;
import rest.model.request.Change;

import java.util.List;

public class AlterTableRequest
{
    public String nameChange;
    public List<Change<Column>> columnChange;
    public List<Change<Constraint>> constraintChange;
    public List<Change<Index>> indexChange;

    @Override
    public String toString()
    {
        return "AlterTableRequest{" +
                "rename='" + nameChange + '\'' +
                ", columnChange=" + columnChange +
                ", constraintChange=" + constraintChange +
                ", indexChange=" + indexChange +
                '}';
    }

    public boolean isNullOrEmpty()
    {
        return (nameChange != null && !nameChange.isEmpty()) &&
                (columnChange == null || columnChange.isEmpty()) &&
                (constraintChange == null || constraintChange.isEmpty()) &&
                (indexChange == null || indexChange.isEmpty());
    }
}
