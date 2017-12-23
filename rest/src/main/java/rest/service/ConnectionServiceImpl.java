package rest.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import rest.dao.ConnectionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.model.ConnectionAuthInfo;

@Service
public class ConnectionServiceImpl implements ConnectionService
{
    private ConnectionDAO connectionDAO;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;

    @Autowired
    ConnectionServiceImpl(ConnectionDAO connectionDAO,
                          AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService)
    {
        this.connectionDAO = connectionDAO;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    public void setConnectionAuthInfo(ConnectionAuthInfo connectionAuthInfo) throws DataAccessException, IllegalArgumentException
    {
        //String originalPassword = connectionAuthInfo.getPassword();
        connectionDAO.setAuthInfo(connectionAuthInfo);
        //autoLogin(connectionAuthInfo.getUsername(), originalPassword);
    }

    public boolean isConnected()
    {
        return SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                //when Anonymous Authentication is enabled
                !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken);
    }

    private void autoLogin(String username, String password)
    {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());

        authenticationManager.authenticate(token);

        if (token.isAuthenticated())
        {
            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }
}