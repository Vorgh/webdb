package rest.service;

import rest.model.connection.UserConnection;
import rest.model.database.View;
import rest.model.database.View;
import rest.model.request.Change;

import java.util.List;

public interface ViewService
{
    List<View> getAllViewsMetadata(String schemaName, UserConnection connection);
    View getViewMetadata(String schemaName, String viewName, UserConnection connection);
    void alterView(Change<View> request, UserConnection connection);
    void createView(View view, UserConnection connection);
    void dropView(String schema, String table, UserConnection connection);
}
