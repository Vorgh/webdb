package rest.service.impl;

import org.springframework.stereotype.Service;
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
}