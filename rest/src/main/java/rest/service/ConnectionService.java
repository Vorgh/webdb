package rest.service;

import org.springframework.dao.DataAccessException;
import rest.model.ConnectionAuthInfo;

public interface ConnectionService
{
    void setConnectionAuthInfo(ConnectionAuthInfo connectionAuthInfo) throws DataAccessException, IllegalArgumentException;
    boolean isConnected();
}