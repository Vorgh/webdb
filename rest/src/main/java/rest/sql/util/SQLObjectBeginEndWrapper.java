package rest.sql.util;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class SQLObjectBeginEndWrapper
{
    public String wrap(@NotNull String input)
    {
        if (!(input.toUpperCase().startsWith("BEGIN") && input.toUpperCase().endsWith("END")))
        {
            return "BEGIN\n" + input.trim() + "\nEND";
        }

        return input;
    }

    public String unwrap(@NotNull String input)
    {
        if (input.toUpperCase().startsWith("BEGIN") && input.toUpperCase().endsWith("END"))
        {
            return input.trim().replaceAll("BEGIN\\s+|\\s+END", "");
        }

        return input;
    }
}
