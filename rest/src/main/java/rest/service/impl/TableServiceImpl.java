package rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import rest.dao.TableDAO;
import rest.exception.ObjectNotFoundException;
import rest.model.connection.UserConnection;
import rest.model.database.*;
import rest.model.request.table.AlterTableRequest;
import rest.model.request.table.CreateTableRequest;
import rest.model.request.table.RowModifyRequest;
import rest.service.validator.SchemaAssert;
import rest.service.TableService;
import rest.service.validator.TableAssert;

import java.util.List;
import java.util.Map;

@Service
public class TableServiceImpl implements TableService
{
    private SchemaAssert schemaAssert;
    private TableAssert tableAssert;

    @Autowired
    public TableServiceImpl(SchemaAssert schemaAssert, TableAssert tableAssert)
    {
        this.schemaAssert = schemaAssert;
        this.tableAssert = tableAssert;
    }

    @Override
    public List<Table> getAllTablesMetadata(String schemaName, UserConnection connection) throws ClassCastException
    {
        tableAssert.assertParams(schemaName);
        schemaAssert.doesSchemaExist(schemaName, connection);
                
        TableDAO tableDAO = new TableDAO(connection);
        return tableDAO.getAllTablesMetadata(schemaName);
        }

    @Override
    public Table getTableMetadata(String schemaName, String tableName, UserConnection connection)
    {
        tableAssert.assertParams(schemaName, tableName);
        schemaAssert.doesSchemaExist(schemaName, connection);
        
        try
        {
            TableDAO tableDAO = new TableDAO(connection);
            return tableDAO.getTableMetadata(schemaName, tableName);
        }
        catch (EmptyResultDataAccessException e)
        {
            throw new ObjectNotFoundException(tableName + " does not exist.");
        }
    }

    @Override
    public List<Column> getAllColumnsMetadata(String schemaName, String tableName, UserConnection connection)
    {
        tableAssert.assertParams(schemaName, tableName);
        schemaAssert.doesSchemaExist(schemaName, connection);

        TableDAO tableDAO = new TableDAO(connection);
        return tableDAO.getAllColumnsMetadata(schemaName, tableName);
    }

    @Override
    public List<Constraint> getForeignKeys(String schemaName, String tableName, UserConnection connection)
    {
        tableAssert.assertParams(schemaName, tableName);
        schemaAssert.doesSchemaExist(schemaName, connection);

        TableDAO tableDAO = new TableDAO(connection);
        return tableDAO.getForeignKeys(schemaName, tableName);
    }

    @Override
    public List<Index> getTableIndexes(String schemaName, String tableName, UserConnection connection)
    {
        tableAssert.assertParams(schemaName, tableName);
        schemaAssert.doesSchemaExist(schemaName, connection);

        TableDAO tableDAO = new TableDAO(connection);
        return tableDAO.getTableIndexes(schemaName, tableName);
    }

    @Override
    public List<Map<String, Object>> getRowData(String schema, String table, String column, UserConnection connection)
    {
        tableAssert.assertParams(schema, table, column);
        schemaAssert.doesSchemaExist(schema, connection);

        TableDAO tableDAO = new TableDAO(connection);
        return tableDAO.getRowData(schema, table, column);
    }

    @Override
    public List<Map<String, Object>> getRowData(String schema, String table, String[] columns, UserConnection connection)
    {
        tableAssert.assertParams(schema, table);
        Assert.notEmpty(columns, "Missing column names.");
        schemaAssert.doesSchemaExist(schema, connection);

        TableDAO tableDAO = new TableDAO(connection);
        return tableDAO.getRowData(schema, table, columns);
    }

    @Override
    public void modifyRows(String schema, String table, RowModifyRequest request, UserConnection connection)
    {
        tableAssert.assertParams(schema, table);
        Assert.notNull(request, "Null row modify request param!");
        Assert.notEmpty(request.changes, "Empty changes map!");
        schemaAssert.doesSchemaExist(schema, connection);

        TableDAO tableDAO = new TableDAO(connection);
        tableDAO.modifyRows(schema, table, request);
    }

    @Override
    public void alterTable(String schema, String table, AlterTableRequest request, UserConnection connection)
    {
        tableAssert.assertParams(schema, table);
        Assert.notNull(request, "Null alter table request!");
        tableAssert.assertNameChange(request.nameChange);
        tableAssert.assertColumnChanges(request.columnChange);
        tableAssert.assertConstraintChanges(request.constraintChange);
        tableAssert.assertIndexChanges(request.indexChange);
        schemaAssert.doesSchemaExist(schema, connection);
        
        TableDAO tableDAO = new TableDAO(connection);
        tableDAO.alterTable(schema, table, request);
    }

    @Override
    public void createTable(CreateTableRequest request, UserConnection connection)
    {
        tableAssert.assertCreateRequest(request);
        schemaAssert.doesSchemaExist(request.schemaName, connection);

        TableDAO tableDAO = new TableDAO(connection);
        tableDAO.createTable(request);
    }

    @Override
    public void dropTable(String schema, String table, UserConnection connection)
    {
        tableAssert.assertParams(schema, table);
        schemaAssert.doesSchemaExist(schema, connection);
        
        TableDAO tableDAO = new TableDAO(connection);
        tableDAO.dropTable(schema, table);
    }
}
