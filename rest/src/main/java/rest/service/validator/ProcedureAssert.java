package rest.service.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import rest.model.database.Procedure;

@Component
public class ProcedureAssert
{
    public void assertParams(String schemaName, String procedureName)
    {
        Assert.hasLength(schemaName, "Missing schema name");
        Assert.hasLength(procedureName, "Missing procedure name");
    }

    public void assertProcedure(Procedure procedure)
    {
        Assert.notNull(procedure, "Null procedure parameter");
        Assert.notNull(procedure.getSchema(), "The schema attribute cannot be null.");
        Assert.isTrue("procedure".equalsIgnoreCase(procedure.getType()) ||
                        "function".equalsIgnoreCase(procedure.getType()),
                "Wrong type attribute. It must be either 'procedure' or 'function'");
        Assert.notNull(procedure.getBody(), "The body attribute cannot be null.");
        Assert.notNull(procedure.getName(), "The name attribute cannot be null.");
    }
}
