package rest.dao;

import rest.model.database.Table;

import java.sql.SQLException;
import java.util.List;

public interface TableDAO
{
    List<Table> getAllTables(String schemaName);
    List<Table> getAllTablesDetail(String schemaName);
    List<Table> getAllViews(String schemaName);
    List<Table> getAllViewsDetail(String schemaName);
}
