package rest.service;

import rest.model.connection.UserConnection;
import rest.model.database.Column;
import rest.model.database.Constraint;
import rest.model.database.Index;
import rest.model.database.Table;
import rest.model.request.table.alter.AlterTableRequest;

import java.util.List;
import java.util.Map;

public interface TableService
{
    List<Table> getAllTablesMetadata(String schemaName, boolean isView, UserConnection connection);
    Table getTableMetadata(String schemaName, String tableName, boolean isView, UserConnection connection);
    List<Column> getAllColumnsMetadata(String schemaName, String tableName, UserConnection connection);
    List<Constraint> getForeignKeys(String schemaName, String tableName, UserConnection connection);
    List<Index> getTableIndexes(String schemaName, String tableName, UserConnection connection);
    List<Map<String, Object>> getRowData(String schema, String table, String column, UserConnection connection);
    List<Map<String, Object>> getRowData(String schema, String table, String[] columns, UserConnection connection);
    void alterTable(String schema, String table, AlterTableRequest request, UserConnection connection);
}
