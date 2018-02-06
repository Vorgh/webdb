package rest.service.impl;

import org.springframework.stereotype.Service;
import rest.dao.TableDAO;
import rest.model.connection.UserConnection;
import rest.model.database.Column;
import rest.model.database.Constraint;
import rest.model.database.Index;
import rest.model.database.Table;
import rest.model.request.table.alter.AlterTableRequest;
import rest.model.request.table.create.CreateTableRequest;
import rest.service.TableService;

import java.util.List;
import java.util.Map;

@Service
public class TableServiceImpl implements TableService
{
    @Override
    public List<Table> getAllTablesMetadata(String schemaName, boolean isView, UserConnection connection) throws ClassCastException
    {
        TableDAO tableDAO = new TableDAO(connection);

        if (!isView)
            return tableDAO.getAllTablesMetadata(schemaName);
        else
            return tableDAO.getAllViewsMetadata(schemaName);
    }

    @Override
    public Table getTableMetadata(String schemaName, String tableName, boolean isView, UserConnection connection)
    {
        TableDAO tableDAO = new TableDAO(connection);

        if (!isView)
            return tableDAO.getTableMetadata(schemaName, tableName);
        else
            return tableDAO.getViewMetadata(schemaName, tableName);
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
}
