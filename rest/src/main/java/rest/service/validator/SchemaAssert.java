package rest.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rest.exception.SchemaNotFoundException;
import rest.model.connection.UserConnection;
import rest.model.database.Schema;
import rest.service.SchemaService;

import java.util.List;

@Component
public class SchemaAssert
{
    private SchemaService schemaService;

    @Autowired
    public SchemaAssert(SchemaService schemaService)
    {
        this.schemaService = schemaService;
    }

    public void doesSchemaExist(String schemaName, UserConnection connection)
    {
        List<Schema> schemaList = schemaService.getAllSchemasMetadata(connection);

        if (schemaList.stream().noneMatch(schema -> schema.getName().equals(schemaName)))
        {
            throw new SchemaNotFoundException(schemaName + " does not exist.");
        }
    }
}
