package rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.dao.TableDAO;
import rest.model.database.Table;

import java.sql.SQLException;
import java.util.List;

@Service
public class DatabaseServiceImpl implements DatabaseService
{
    private TableDAO tableDAO;

    @Autowired
    DatabaseServiceImpl(TableDAO tableDAO)
    {
        this.tableDAO = tableDAO;
    }

    public List<Table> getAllTables(boolean includeDetails, boolean isView)
    {
        List<Table> tables;
        if (!includeDetails)
        {
            if (!isView)
                tables = tableDAO.getAllTables("webdb");
            else
                tables = tableDAO.getAllViews("webdb");
        }
        else
        {
            if (!isView)
                tables = tableDAO.getAllTablesDetail("webdb");
            else
                tables = tableDAO.getAllViewsDetail("webdb");
        }

        return tables;
    }
}
