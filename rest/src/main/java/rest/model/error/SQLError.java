package rest.model.error;

public class SQLError extends AbstractError
{
    private int code;

    public SQLError(String name, String message)
    {
        super(name, message);
    }

    public SQLError(String name, String message, int code)
    {
        super(name, message);
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }
}
