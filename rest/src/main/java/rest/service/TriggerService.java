package rest.service;

import rest.model.connection.UserConnection;
import rest.model.database.Trigger;

import java.util.List;

public interface TriggerService
{
    List<Trigger> getAllTriggers(String schemaName, UserConnection connection);
    Trigger getTrigger(String schemaName, String triggerName, UserConnection connection);
    void createTrigger(String schemaName, Trigger requestTrigger, UserConnection connection);
    void modifyTrigger(String schemaName, Trigger requestTrigger, UserConnection connection);
    void dropTrigger(String schemaName, String trigger, UserConnection connection);
}
