package rest.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.*;
import rest.dao.ConnectionDAO;
import rest.model.connection.ConnectionAuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rest.model.connection.UserConnection;
import rest.service.ConnectionService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("connection")
public class ConnectionController
{
    private ConnectionService connectionService;
    private DefaultTokenServices tokenServices;

    @Autowired
    public ConnectionController(ConnectionService connectionService, DefaultTokenServices tokenServices, ConnectionDAO connectionDAO)
    {
        this.connectionService = connectionService;
        this.tokenServices = tokenServices;
    }

    @PostMapping("connectionAuth")
    public ResponseEntity<Void> setConnectionAuthInfo(@RequestBody ConnectionAuthInfo connAuth)
    {
        connectionService.setConnectionAuthInfo(connAuth);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, @AuthenticationPrincipal UserConnection connection)
    {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.contains("Bearer"))
        {
            String tokenId = authorization.substring("Bearer".length()+1);
            boolean removed = tokenServices.revokeToken(tokenId);

            if (removed)
            {
                connectionService.logout(connection);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}