package rest.sql.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ListBuilder
{
    public List<String> build(String... args)
    {
        return new ArrayList<>(Arrays.asList(args));
    }
}
