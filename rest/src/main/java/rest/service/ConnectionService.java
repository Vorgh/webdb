package rest.service;

import rest.model.connection.ConnectionAuthInfo;

public interface ConnectionService
{
    void setConnectionAuthInfo(ConnectionAuthInfo connectionAuthInfo) throws IllegalStateException, IllegalArgumentException;
}