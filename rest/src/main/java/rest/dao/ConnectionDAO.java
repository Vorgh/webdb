package rest.dao;

import org.springframework.dao.DataAccessException;
import rest.model.connection.ConnectionAuthInfo;
import rest.model.connection.UserConnection;

public interface ConnectionDAO
{
    void setAuthInfo(ConnectionAuthInfo connAuth) throws DataAccessException, IllegalArgumentException;
    UserConnection getConnectedUserByName(String username);
}
