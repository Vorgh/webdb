package rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.dao.ConnectionDAO;
import rest.model.connection.ConnectionAuthInfo;
import rest.model.connection.UserConnection;
import rest.service.ConnectionService;

import java.util.Map;

@Service
public class ConnectionServiceImpl implements ConnectionService
{
    private ConnectionDAO connectionDAO;
    private Map<String, UserConnection> connectedUsers;

    @Autowired
    ConnectionServiceImpl(ConnectionDAO connectionDAO, Map<String, UserConnection> connectedUsers)
    {
        this.connectionDAO = connectionDAO;
        this.connectedUsers = connectedUsers;
    }

    public void setConnectionAuthInfo(ConnectionAuthInfo connectionAuthInfo) throws IllegalStateException, IllegalArgumentException
    {
        connectionDAO.setAuthInfo(connectionAuthInfo);
    }

    public void logout(UserConnection connection)
    {
        String key = connection.getUsername()+"@"+connection.getUrl();
        connectedUsers.remove(key);
    }
}