package rest.exception;

public class SchemaNotFoundException extends RuntimeException
{
    public SchemaNotFoundException()
    {
    }

    public SchemaNotFoundException(String message)
    {
        super(message);
    }
}
