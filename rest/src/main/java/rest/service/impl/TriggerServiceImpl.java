package rest.service.impl;

import org.springframework.stereotype.Service;
import rest.dao.TriggerDAO;
import rest.model.connection.UserConnection;
import rest.model.database.Trigger;
import rest.service.TriggerService;

import java.util.List;

@Service
public class TriggerServiceImpl implements TriggerService
{
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

        triggerDAO.createTrigger(schemaName, requestTrigger);
    }

    @Override
    public void modifyTrigger(String schemaName, Trigger requestTrigger, UserConnection connection)
    {
        TriggerDAO triggerDAO = new TriggerDAO(connection);

        triggerDAO.modifyTrigger(schemaName, requestTrigger);
    }

    @Override
    public void dropTrigger(String schemaName, String triggerName, UserConnection connection)
    {
        TriggerDAO triggerDAO = new TriggerDAO(connection);

        triggerDAO.dropTrigger(schemaName, triggerName);
    }
}
