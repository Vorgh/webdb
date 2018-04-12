package rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import rest.exception.ObjectNotFoundException;
import rest.exception.SchemaNotFoundException;
import rest.model.request.Change;
import rest.service.validator.ProcedureAssert;
import rest.service.validator.SchemaAssert;
import rest.sql.util.SQLObjectBeginEndWrapper;
import rest.dao.ProcedureDAO;
import rest.model.connection.UserConnection;
import rest.model.database.Procedure;
import rest.service.ProcedureService;

import java.util.List;

@Service
public class ProcedureServiceImpl implements ProcedureService
{
    private SchemaAssert schemaAssert;
    private ProcedureAssert procedureAssert;
    private SQLObjectBeginEndWrapper beginEndWrapper;

    @Autowired
    public ProcedureServiceImpl(SQLObjectBeginEndWrapper beginEndWrapper, SchemaAssert schemaAssert, ProcedureAssert procedureAssert)
    {
        this.beginEndWrapper = beginEndWrapper;
        this.schemaAssert = schemaAssert;
        this.procedureAssert = procedureAssert;
    }

    @Override
    public List<Procedure> getAllProcedures(String schemaName, UserConnection connection)
    {
        Assert.hasLength(schemaName, "Missing schema name");
        schemaAssert.doesSchemaExist(schemaName, connection);
        
        ProcedureDAO procedureDAO = new ProcedureDAO(connection);
        return procedureDAO.getAllProcedures(schemaName);
    }

    @Override
    public Procedure getProcedure(String schemaName, String procedureName, UserConnection connection)
    {
        Assert.hasLength(schemaName, "Missing schema name");
        Assert.hasLength(procedureName, "Missing procedure name");
        schemaAssert.doesSchemaExist(schemaName, connection);

        try
        {
            ProcedureDAO procedureDAO = new ProcedureDAO(connection);
            return procedureDAO.getProcedure(schemaName, procedureName);
        }
        catch (EmptyResultDataAccessException e)
        {
            throw new ObjectNotFoundException(procedureName + " does not exist.");
        }
    }

    @Override
    public void createProcedure(Procedure procedure, UserConnection connection)
    {
        procedureAssert.assertProcedure(procedure);
        schemaAssert.doesSchemaExist(procedure.getSchema(), connection);

        ProcedureDAO procedureDAO = new ProcedureDAO(connection);
        procedure.setBody(beginEndWrapper.wrap(procedure.getBody()));
        procedureDAO.createProcedure(procedure);
    }

    @Override
    public void modifyProcedure(Change<Procedure> change, UserConnection connection)
    {
        procedureAssert.assertProcedure(change.from);
        procedureAssert.assertProcedure(change.to);
        schemaAssert.doesSchemaExist(change.from.getSchema(), connection);
        schemaAssert.doesSchemaExist(change.to.getSchema(), connection);

        ProcedureDAO procedureDAO = new ProcedureDAO(connection);
        change.from.setBody(beginEndWrapper.wrap(change.from.getBody()));
        change.to.setBody(beginEndWrapper.wrap(change.to.getBody()));
        procedureDAO.modifyProcedure(change);
    }

    @Override
    public void dropProcedure(String schemaName, String procedureName, UserConnection connection)
    {
        procedureAssert.assertParams(schemaName, procedureName);

        ProcedureDAO procedureDAO = new ProcedureDAO(connection);
        procedureDAO.dropProcedure(schemaName, procedureName);
    }
}
