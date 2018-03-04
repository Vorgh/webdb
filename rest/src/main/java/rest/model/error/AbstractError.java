package rest.model.error;

public abstract class AbstractError
{
    private String name;
    private String message;

    AbstractError(String name, String message)
    {
        this.name = name;
        this.message = message;
    }

    public String getName()
    {
        return name;
    }

    public String getMessage()
    {
        return message;
    }
}
