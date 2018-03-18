package rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.model.request.Change;
import rest.sql.util.SQLObjectBeginEndWrapper;
import rest.dao.TriggerDAO;
import rest.model.connection.UserConnection;
import rest.model.database.Trigger;
import rest.service.TriggerService;

import java.util.List;

@Service
public class TriggerServiceImpl implements TriggerService
{
    private SQLObjectBeginEndWrapper beginEndWrapper;

    @Autowired
    public TriggerServiceImpl(SQLObjectBeginEndWrapper beginEndWrapper)
    {
        this.beginEndWrapper = beginEndWrapper;
    }

    @Override
    public List<Trigger> getAllTriggers(String schemaName, UserConnection connection)
    {
        TriggerDAO triggerDAO = new TriggerDAO(connection);

        return triggerDAO.getAllTriggers(schemaName);
    }

    @Override
    public Trigger getTrigger(String schemaName, String triggerName, UserConnection connection)
    {
        TriggerDAO triggerDAO = new TriggerDAO(connection);

        return triggerDAO.getTrigger(schemaName, triggerName);
    }

    @Override
    public void createTrigger(String schemaName, Trigger requestTrigger, UserConnection connection)
    {
        TriggerDAO triggerDAO = new TriggerDAO(connection);

        requestTrigger.triggerBody = beginEndWrapper.wrap(requestTrigger.triggerBody);
        triggerDAO.createTrigger(requestTrigger);
    }

    @Override
    public void modifyTrigger(Change<Trigger> request, UserConnection connection)
    {
        TriggerDAO triggerDAO = new TriggerDAO(connection);

        request.to.triggerBody = beginEndWrapper.wrap(request.to.triggerBody);
        triggerDAO.modifyTrigger(request);
    }

    @Override
    public void dropTrigger(String schemaName, String triggerName, UserConnection connection)
    {
        TriggerDAO triggerDAO = new TriggerDAO(connection);

        triggerDAO.dropTrigger(schemaName, triggerName);
    }
}
