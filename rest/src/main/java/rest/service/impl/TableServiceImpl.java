package rest.service.impl;

import org.springframework.stereotype.Service;
import rest.dao.TableDAO;
import rest.model.connection.UserConnection;
import rest.model.database.Column;
import rest.model.database.Table;
import rest.service.TableService;

import java.util.List;

@Service
public class TableServiceImpl implements TableService
{
    public List<Table> getAllTablesMetadata(String schemaName, boolean isView, UserConnection connection) throws ClassCastException
    {
        TableDAO tableDAO = new TableDAO(connection);

        if (!isView)
            return tableDAO.getAllTablesMetadata(schemaName);
        else
            return tableDAO.getAllViewsMetadata(schemaName);

    }

    @Override
    public List<Column> getAllColumnsMetadata(String schemaName, String tableName, UserConnection connection)
    {
        TableDAO tableDAO = new TableDAO(connection);

        return tableDAO.getAllColumnsMetadata(schemaName, tableName);
    }
}
