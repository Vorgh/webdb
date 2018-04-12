package rest.exception;

public class InvalidJdbcUrlException extends RuntimeException
{
    public InvalidJdbcUrlException()
    {
    }

    public InvalidJdbcUrlException(String message)
    {
        super(message);
    }
}
