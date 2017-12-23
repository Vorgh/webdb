package rest.dao;

import org.springframework.dao.DataAccessException;
import rest.model.ConnectionAuthInfo;
import rest.model.UserConnection;

public interface ConnectionDAO
{
    void setAuthInfo(ConnectionAuthInfo connAuth) throws DataAccessException, IllegalArgumentException;
    UserConnection getConnectedUserByName(String username);
}
