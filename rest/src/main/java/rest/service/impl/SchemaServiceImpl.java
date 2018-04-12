package rest.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import rest.dao.SchemaDAO;
import rest.model.connection.UserConnection;
import rest.model.database.Schema;
import rest.service.SchemaService;

import java.util.List;

@Service
public class SchemaServiceImpl implements SchemaService
{
    @Override
    public List<Schema> getAllSchemasMetadata(UserConnection connection)
    {
        SchemaDAO schemaDAO = new SchemaDAO(connection);

        return schemaDAO.getAllSchemasMetadata();
    }

    @Override
    public void createSchema(String schemaName, UserConnection connection)
    {
        Assert.hasLength(schemaName, "Missing schema name.");

        SchemaDAO schemaDAO = new SchemaDAO(connection);
        schemaDAO.createSchema(schemaName);
    }

    @Override
    public void dropSchema(String schemaName, UserConnection connection)
    {
        Assert.hasLength(schemaName, "Missing schema name.");

        SchemaDAO schemaDAO = new SchemaDAO(connection);
        schemaDAO.dropSchema(schemaName);
    }
}