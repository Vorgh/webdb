package rest.service.impl;

import org.springframework.stereotype.Service;
import rest.dao.TableDAO;
import rest.model.connection.UserConnection;
import rest.model.database.Column;
import rest.model.database.Constraint;
import rest.model.database.Index;
import rest.model.database.Table;
import rest.model.request.table.AlterTableRequest;
import rest.model.request.table.CreateTableRequest;
import rest.model.request.table.RowModifyRequest;
import rest.service.TableService;

import java.util.List;
import java.util.Map;

@Service
public class TableServiceImpl implements TableService
{
    @Override
    public List<Table> getAllTablesMetadata(String schemaName, UserConnection connection) throws ClassCastException
    {
        TableDAO tableDAO = new TableDAO(connection);

        return tableDAO.getAllTablesMetadata(schemaName);
    }

    @Override
    public Table getTableMetadata(String schemaName, String tableName, UserConnection connection)
    {
        TableDAO tableDAO = new TableDAO(connection);

        return tableDAO.getTableMetadata(schemaName, tableName);
    }

    @Override
    public List<Column> getAllColumnsMetadata(String schemaName, String tableName, UserConnection connection)
    {
        TableDAO tableDAO = new TableDAO(connection);

        return tableDAO.getAllColumnsMetadata(schemaName, tableName);
    }

    @Override
    public List<Constraint> getForeignKeys(String schemaName, String tableName, UserConnection connection)
    {
        TableDAO tableDAO = new TableDAO(connection);

        return tableDAO.getForeignKeys(schemaName, tableName);
    }

    @Override
    public List<Index> getTableIndexes(String schemaName, String tableName, UserConnection connection)
    {
        TableDAO tableDAO = new TableDAO(connection);

        return tableDAO.getTableIndexes(schemaName, tableName);
    }

    @Override
    public List<Map<String, Object>> getRowData(String schema, String table, String column, UserConnection connection)
    {
        TableDAO tableDAO = new TableDAO(connection);

        return tableDAO.getRowData(schema, table, column);
    }

    @Override
    public List<Map<String, Object>> getRowData(String schema, String table, String[] columns, UserConnection connection)
    {
        TableDAO tableDAO = new TableDAO(connection);

        return tableDAO.getRowData(schema, table, columns);
    }

    @Override
    public void modifyRows(String schema, String table, RowModifyRequest request, UserConnection connection)
    {
        TableDAO tableDAO = new TableDAO(connection);

        tableDAO.modifyRows(schema, table, request);
    }

    @Override
    public void alterTable(String schema, String table, AlterTableRequest request, UserConnection connection)
    {
        TableDAO tableDAO = new TableDAO(connection);

        tableDAO.alterTable(schema, table, request);
    }

    @Override
    public void createTable(String schema, CreateTableRequest request, UserConnection connection)
    {
        TableDAO tableDAO = new TableDAO(connection);

        tableDAO.createTable(schema, request);
    }

    @Override
    public void dropTable(String schema, String table, UserConnection connection)
    {
        TableDAO tableDAO = new TableDAO(connection);

        tableDAO.dropTable(schema, table);
    }
}
