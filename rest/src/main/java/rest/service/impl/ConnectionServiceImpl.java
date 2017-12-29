package rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.dao.ConnectionDAO;
import rest.model.connection.ConnectionAuthInfo;
import rest.service.ConnectionService;

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