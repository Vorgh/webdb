package rest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import rest.dao.ConnectionDAO;
import rest.model.connection.UserConnection;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Component
public class UrlWithNameAuthenticationProvider implements AuthenticationProvider
{
    @Autowired
    private ConnectionDAO connectionDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        HashMap details = (LinkedHashMap)authentication.getDetails();
        String name = authentication.getName();
        String password = (String)authentication.getCredentials();
        String url = (String)details.get("url");

        UserConnection connection = connectionDAO.getConnectedUserByDomainAndName(url, name);
        if (connection == null)
        {
            throw new BadCredentialsException("User not found.");
        }

        if (!password.equals(connection.getPassword()) && !url.equals(connection.getUrl()))
        {
            throw new BadCredentialsException("Wrong credentials.");
        }

        Collection<GrantedAuthority> authorities = connection.getAuthorities();

        return new UsernamePasswordAuthenticationToken(connection, passwordEncoder.encode(password), authorities);
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
