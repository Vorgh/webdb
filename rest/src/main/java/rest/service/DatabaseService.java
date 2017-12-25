package rest.service;

import rest.model.database.Table;

import java.util.List;

public interface DatabaseService
{
    List<Table> getAllTables(boolean includeDetails, boolean isView);
}
