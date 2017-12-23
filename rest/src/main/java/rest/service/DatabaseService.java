package rest.service;

import rest.model.Table;

import java.util.List;

public interface DatabaseService
{
    List<Table> getAllTables();
}
