package rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import rest.dao.TableDAO;
import rest.exception.ObjectNotFoundException;
import rest.model.request.Change;
import rest.service.validator.SchemaAssert;
import rest.service.validator.TriggerAssert;
import rest.sql.util.SQLObjectBeginEndWrapper;
import rest.dao.TriggerDAO;
import rest.model.connection.UserConnection;
import rest.model.database.Trigger;
import rest.service.TriggerService;

import java.util.List;

@Service
public class TriggerServiceImpl implements TriggerService
{
    private SchemaAssert schemaAssert;
    private TriggerAssert triggerAssert;
    private SQLObjectBeginEndWrapper beginEndWrapper;

    @Autowired
    public TriggerServiceImpl(SchemaAssert schemaAssert, TriggerAssert triggerAssert, SQLObjectBeginEndWrapper beginEndWrapper)
    {
        this.schemaAssert = schemaAssert;
        this.triggerAssert = triggerAssert;
        this.beginEndWrapper = beginEndWrapper;
    }

    @Override
    public List<Trigger> getAllTriggers(String schemaName, UserConnection connection)
    {
        Assert.hasLength(schemaName, "Schema name is missing!");
        schemaAssert.doesSchemaExist(schemaName, connection);

        TriggerDAO triggerDAO = new TriggerDAO(connection);
        return triggerDAO.getAllTriggers(schemaName);
    }

    @Override
    public Trigger getTrigger(String schemaName, String triggerName, UserConnection connection)
    {
        triggerAssert.assertParams(schemaName, triggerName);
        schemaAssert.doesSchemaExist(schemaName, connection);

        try
        {
            TriggerDAO triggerDAO = new TriggerDAO(connection);
            return triggerDAO.getTrigger(schemaName, triggerName);
        }
        catch (EmptyResultDataAccessException e)
        {
            throw new ObjectNotFoundException(triggerName + " does not exist.");
        }
    }

    @Override
    public void createTrigger(Trigger requestTrigger, UserConnection connection)
    {
        triggerAssert.assertParams(requestTrigger);
        schemaAssert.doesSchemaExist(requestTrigger.schema, connection);

        TriggerDAO triggerDAO = new TriggerDAO(connection);
        requestTrigger.triggerBody = beginEndWrapper.wrap(requestTrigger.triggerBody);
        triggerDAO.createTrigger(requestTrigger);
    }

    @Override
    public void modifyTrigger(Change<Trigger> request, UserConnection connection)
    {
        triggerAssert.assertParams(request.from.schema, request.from.name);
        triggerAssert.assertParams(request.to);
        schemaAssert.doesSchemaExist(request.from.schema, connection);
        schemaAssert.doesSchemaExist(request.to.schema, connection);

        TriggerDAO triggerDAO = new TriggerDAO(connection);
        request.to.triggerBody = beginEndWrapper.wrap(request.to.triggerBody);
        triggerDAO.modifyTrigger(request);
    }

    @Override
    public void dropTrigger(String schemaName, String triggerName, UserConnection connection)
    {
        triggerAssert.assertParams(schemaName, triggerName);
        schemaAssert.doesSchemaExist(schemaName, connection);

        TriggerDAO triggerDAO = new TriggerDAO(connection);
        triggerDAO.dropTrigger(schemaName, triggerName);
    }
}
