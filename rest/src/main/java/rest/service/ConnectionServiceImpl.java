package rest.service;

import rest.dao.ConnectionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.model.connection.ConnectionAuthInfo;

@Service
public class ConnectionServiceImpl implements ConnectionService
{
    private ConnectionDAO connectionDAO;

    @Autowired
    ConnectionServiceImpl(ConnectionDAO connectionDAO)
    {
        this.connectionDAO = connectionDAO;
    }

    public void setConnectionAuthInfo(ConnectionAuthInfo connectionAuthInfo) throws IllegalStateException, IllegalArgumentException
    {
        connectionDAO.setAuthInfo(connectionAuthInfo);
    }
}