package rest.exception;

public class MissingAuthInfoException extends RuntimeException
{
    public MissingAuthInfoException()
    {
    }

    public MissingAuthInfoException(String message)
    {
        super(message);
    }
}
