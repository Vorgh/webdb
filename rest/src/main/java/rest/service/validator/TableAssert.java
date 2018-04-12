package rest.service.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import rest.model.database.Column;
import rest.model.database.Constraint;
import rest.model.database.Index;
import rest.model.request.Change;
import rest.model.request.table.CreateTableRequest;

import java.util.List;

@Component
public class TableAssert
{
    public void assertParams(String schemaName)
    {
        Assert.hasLength(schemaName, "Missing schema name.");
    }

    public void assertParams(String schemaName, String tableName)
    {
        Assert.hasLength(schemaName, "Missing schema name.");
        Assert.hasLength(tableName, "Missing table name.");
    }

    public void assertParams(String schemaName, String tableName, String columName)
    {
        Assert.hasLength(schemaName, "Missing schema name.");
        Assert.hasLength(tableName, "Missing table name.");
        Assert.hasLength(columName, "Missing column name.");
    }

    public void assertNameChange(String nameChange)
    {
        if (nameChange != null)
        {
            Assert.hasLength(nameChange, "New table name cannot be empty!");
        }
    }

    public void assertColumnChanges(List<Change<Column>> changes)
    {
        if (changes != null)
        {
            for (Change<?> change : changes)
            {
                Assert.isTrue(!(change.from == null && change.to == null), "Empty column change object in: " + changes.toString());
                if (change.from != null)
                {
                    Assert.isInstanceOf(Column.class, change.from);
                }
                if (change.to != null)
                {
                    Assert.isInstanceOf(Column.class, change.to);
                }
            }
        }
    }

    public void assertConstraintChanges(List<Change<Constraint>> changes)
    {
        if (changes != null)
        {
            for (Change<?> change : changes)
            {
                Assert.isTrue(!(change.from == null && change.to == null), "Empty constraint change object in: " + changes.toString());
                if (change.from != null)
                {
                    Assert.isInstanceOf(Constraint.class, change.from);
                }
                if (change.to != null)
                {
                    Assert.isInstanceOf(Constraint.class, change.to);
                }
            }
        }
    }

    public void assertIndexChanges(List<Change<Index>> changes)
    {
        if (changes != null)
        {
            for (Change<?> change : changes)
            {
                Assert.isTrue(!(change.from == null && change.to == null), "Empty index change object in: " + changes.toString());
                if (change.from != null)
                {
                    Assert.isInstanceOf(Index.class, change.from);
                }
                if (change.to != null)
                {
                    Assert.isInstanceOf(Index.class, change.to);
                }
            }
        }
    }
    
    public void assertCreateRequest(CreateTableRequest request)
    {
        Assert.hasLength(request.schemaName, "Schema name is missing!");
        Assert.hasLength(request.tableName, "Table name is missing!");
        Assert.notNull(request.columns, "Null column list parameter!");
        Assert.notEmpty(request.columns, "Empty column list!");
        Assert.notNull(request.foreignKeys, "Null foreign key list parameter!");
    }
}
