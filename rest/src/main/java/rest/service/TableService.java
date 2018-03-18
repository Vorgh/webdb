package rest.service;

import rest.model.connection.UserConnection;
import rest.model.database.Column;
import rest.model.database.Constraint;
import rest.model.database.Index;
import rest.model.database.Table;
import rest.model.request.table.AlterTableRequest;
import rest.model.request.table.CreateTableRequest;
import rest.model.request.table.RowModifyRequest;

import java.util.List;
import java.util.Map;

public interface TableService
{
    List<Table> getAllTablesMetadata(String schemaName, UserConnection connection);
    Table getTableMetadata(String schemaName, String tableName, UserConnection connection);
    List<Column> getAllColumnsMetadata(String schemaName, String tableName, UserConnection connection);
    List<Constraint> getForeignKeys(String schemaName, String tableName, UserConnection connection);
    List<Index> getTableIndexes(String schemaName, String tableName, UserConnection connection);
    List<Map<String, Object>> getRowData(String schema, String table, String column, UserConnection connection);
    List<Map<String, Object>> getRowData(String schema, String table, String[] columns, UserConnection connection);
    void alterTable(String schema, String table, AlterTableRequest request, UserConnection connection);
    void createTable(String schema, CreateTableRequest request, UserConnection connection);
    void dropTable(String schema, String table, UserConnection connection);
    void modifyRows(String schema, String table, RowModifyRequest request, UserConnection connection);
    /*void insertRows(String schema, String table, Rows rows, UserConnection connection);
    void updateRows(String schema, CreateTableRequest request, UserConnection connection);
    void deleteRows(String schema, String table, UserConnection connection);*/
}
