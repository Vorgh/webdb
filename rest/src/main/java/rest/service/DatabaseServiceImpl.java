package rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rest.dao.TableDAO;
import rest.model.Table;

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

    public List<Table> getAllTables()
    {
        List<Table> tables = null;
        try
        {
            tables = tableDAO.getAllTableNames();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return tables;
    }
}
