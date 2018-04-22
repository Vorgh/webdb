package rest.service;

import rest.model.connection.UserConnection;
import rest.model.database.Procedure;
import rest.model.request.Change;

import java.util.List;

public interface ProcedureService
{
    List<Procedure> getAllProcedures(String schemaName, UserConnection connection);
    Procedure getProcedure(String schemaName, String procedureName, String isFunction, UserConnection connection);
    void createProcedure(Procedure procedure, UserConnection connection);
    void modifyProcedure(Change<Procedure> change, UserConnection connection);
    void dropProcedure(String schemaName, String procedureName, String isFunction, UserConnection connection);
}
