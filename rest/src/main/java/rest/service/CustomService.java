package rest.service;

import rest.model.connection.UserConnection;

import java.util.List;
import java.util.Map;

public interface CustomService
{
    List<Map<String, Object>> execute(String sql, UserConnection connection);
}
