package rest.service;

import rest.model.connection.UserConnection;
import rest.model.database.Column;
import rest.model.database.Table;

import java.util.List;

public interface TableService
{
    List<Table> getAllTablesMetadata(String schemaName, boolean isView, UserConnection connection);
    List<Column> getAllColumnsMetadata(String schemaName, String tableName, UserConnection connection);
}
