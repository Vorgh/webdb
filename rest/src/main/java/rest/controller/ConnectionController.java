package rest.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import rest.model.ConnectionAuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rest.service.ConnectionService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("connection")
public class ConnectionController
{
    private ConnectionService connectionService;
    private DefaultTokenServices tokenServices;

    @Autowired
    public ConnectionController(ConnectionService connectionService, DefaultTokenServices tokenServices)
    {
        this.connectionService = connectionService;
        this.tokenServices = tokenServices;
    }

    @GetMapping("isConnected")
    public ResponseEntity<Void> isConnected()
    {
        if (connectionService.isConnected())
            return new ResponseEntity<Void>(HttpStatus.OK);
        else
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("connectionAuth")
    public ResponseEntity<Void> setConnectionAuthInfo(@RequestBody ConnectionAuthInfo connAuth)
    {
        try
        {
            connectionService.setConnectionAuthInfo(connAuth);
        }
        catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (IllegalStateException e)
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        //request.login(connAuth.getUsername(), connAuth.getPassword());

        /*List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication token = new UsernamePasswordAuthenticationToken(connAuth.getUsername(), authorities);
        SecurityContextHolder.getContext().setAuthentication(token);

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());*/

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("revokeToken")
    public ResponseEntity<Void> logout(HttpServletRequest request)
    {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.contains("Bearer"))
        {
            String tokenId = authorization.substring("Bearer".length()+1);
            boolean removed = tokenServices.revokeToken(tokenId);

            if (removed)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}