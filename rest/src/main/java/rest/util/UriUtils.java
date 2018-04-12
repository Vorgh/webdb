package rest.util;

import rest.exception.InvalidJdbcUrlException;

import java.net.URI;
import java.net.URISyntaxException;

public class UriUtils
{
    public static String extractDomainAndPortFromUrl(String url) throws InvalidJdbcUrlException
    {
        URI uri;

        try
        {
            uri = new URI(url.trim().substring(5));
            if (uri.getPort() != -1)
            {
                return uri.getHost() + ":" + uri.getPort();
            }
            else
            {
                return uri.getHost();
            }
        }
        catch (URISyntaxException e)
        {
            throw new InvalidJdbcUrlException(e.getMessage());
        }
    }
}
