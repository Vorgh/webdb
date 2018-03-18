package rest.exception;

public class DatabaseConnectionException extends RuntimeException
{
    public DatabaseConnectionException()
    {

    }

    public DatabaseConnectionException(String message)
    {
        super(message);
    }
}
