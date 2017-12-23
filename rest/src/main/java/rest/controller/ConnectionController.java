package rest.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rest.model.ConnectionAuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rest.service.ConnectionService;

@RestController
@RequestMapping("connection")
public class ConnectionController
{
    @Autowired
    private ConnectionService connectionService;
    @Autowired
    private AuthenticationManager authenticationManager;

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

            /*UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(connAuth.getUsername(), connAuth.getPassword());
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);*/
        }
        catch (DataAccessException e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
}