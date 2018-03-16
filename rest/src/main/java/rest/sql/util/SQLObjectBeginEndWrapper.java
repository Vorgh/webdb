package rest.sql.util;

import org.springframework.stereotype.Component;

@Component
public class SQLObjectBeginEndWrapper
{
    public String wrap(String input)
    {
        String body = input;
        if (body.toUpperCase().startsWith("BEGIN") && body.toUpperCase().endsWith("END"))
        {
            body = body + ";";
        }

        if (!(body.toUpperCase().startsWith("BEGIN") && body.toUpperCase().endsWith("END;")))
        {
            body = "BEGIN\n" + body + "\nEND;";
        }

        return body;
    }
}
