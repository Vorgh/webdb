package rest.service;

import rest.model.connection.ConnectionAuthInfo;
import rest.model.connection.UserConnection;

public interface ConnectionService
{
    void setConnectionAuthInfo(ConnectionAuthInfo connectionAuthInfo) throws IllegalStateException, IllegalArgumentException;
    void logout(UserConnection connection);
}