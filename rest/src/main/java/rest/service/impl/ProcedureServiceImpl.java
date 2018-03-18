package rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.model.request.Change;
import rest.sql.util.SQLObjectBeginEndWrapper;
import rest.dao.ProcedureDAO;
import rest.model.connection.UserConnection;
import rest.model.database.Procedure;
import rest.service.ProcedureService;

import java.util.List;

@Service
public class ProcedureServiceImpl implements ProcedureService
{
    private SQLObjectBeginEndWrapper beginEndWrapper;

    @Autowired
    public ProcedureServiceImpl(SQLObjectBeginEndWrapper beginEndWrapper)
    {
        this.beginEndWrapper = beginEndWrapper;
    }

    @Override
    public List<Procedure> getAllProcedures(String schemaName, UserConnection connection)
    {
        ProcedureDAO procedureDAO = new ProcedureDAO(connection);

        return procedureDAO.getAllProcedures(schemaName);
    }

    @Override
    public Procedure getProcedure(String schemaName, String procedureName, UserConnection connection)
    {
        ProcedureDAO procedureDAO = new ProcedureDAO(connection);

        return procedureDAO.getProcedure(schemaName, procedureName);
    }

    @Override
    public void createProcedure(Procedure procedure, UserConnection connection)
    {
        ProcedureDAO procedureDAO = new ProcedureDAO(connection);

        procedure.setBody(beginEndWrapper.wrap(procedure.getBody()));

        procedureDAO.createProcedure(procedure);
    }

    @Override
    public void modifyProcedure(Change<Procedure> change, UserConnection connection)
    {
        ProcedureDAO procedureDAO = new ProcedureDAO(connection);

        change.to.setBody(beginEndWrapper.wrap(change.to.getBody()));

        procedureDAO.modifyProcedure(change);
    }

    @Override
    public void dropProcedure(String schemaName, String procedureName, UserConnection connection)
    {
        ProcedureDAO procedureDAO = new ProcedureDAO(connection);

        procedureDAO.dropProcedure(schemaName, procedureName);
    }
}
