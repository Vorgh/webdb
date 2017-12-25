package rest.service;

import org.springframework.dao.DataAccessException;
import rest.model.connection.ConnectionAuthInfo;

public interface ConnectionService
{
    void setConnectionAuthInfo(ConnectionAuthInfo connectionAuthInfo) throws DataAccessException, IllegalArgumentException;
}