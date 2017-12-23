package rest.dao;

import rest.model.Table;

import java.sql.SQLException;
import java.util.List;

public interface TableDAO
{
    List<Table> getAllTableNames() throws SQLException;
}
