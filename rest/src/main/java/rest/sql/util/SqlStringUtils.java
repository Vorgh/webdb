package rest.sql.util;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public class SqlStringUtils
{
    public static String escape(String s)
    {
        return StringEscapeUtils.escapeJava(s);
    }

    public static String quote(String s)
    {
        if (s == null || s.isEmpty() || StringUtils.isNumeric(s) || (s.startsWith("'") && s.endsWith("'")))
        {
            return s;
        }

        return "'" + s + "'";
    }

    public static String bquote(String s)
    {
        if (s == null || s.isEmpty() || (s.startsWith("`") && s.endsWith("`")))
        {
            return s;
        }

        return "`" + s + "`";
    }
}
