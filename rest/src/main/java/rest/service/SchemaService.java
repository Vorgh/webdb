package rest.service;

import rest.model.connection.UserConnection;
import rest.model.database.Schema;

import java.util.List;

public interface SchemaService
{
    List<Schema> getAllSchemasMetadata(UserConnection connection);
}
