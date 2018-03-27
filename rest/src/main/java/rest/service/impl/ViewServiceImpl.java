package rest.service.impl;

import org.springframework.stereotype.Service;
import rest.dao.ViewDAO;
import rest.model.connection.UserConnection;
import rest.model.database.View;
import rest.model.request.Change;
import rest.service.ViewService;

import java.util.List;

@Service
public class ViewServiceImpl implements ViewService
{
    @Override
    public List<View> getAllViewsMetadata(String schemaName, UserConnection connection)
    {
        ViewDAO viewDAO = new ViewDAO(connection);

        return viewDAO.getAllViewsMetadata(schemaName);
    }

    @Override
    public View getViewMetadata(String schemaName, String viewName, UserConnection connection)
    {
        ViewDAO viewDAO = new ViewDAO(connection);

        return viewDAO.getViewMetadata(schemaName, viewName);
    }

    @Override
    public void alterView(Change<View> request, UserConnection connection)
    {
        ViewDAO viewDAO = new ViewDAO(connection);
        String selectQuery = request.to.getSelectQuery();

        if (selectQuery.endsWith(";"))
        {
            request.to.setSelectQuery(selectQuery.substring(0, selectQuery.length() - 1));
        }

        viewDAO.alterView(request);
    }

    @Override
    public void createView(View view, UserConnection connection)
    {
        ViewDAO viewDAO = new ViewDAO(connection);
        String selectQuery = view.getSelectQuery();

        if (view.getSelectQuery().endsWith(";"))
        {
            view.setSelectQuery(selectQuery.substring(0, selectQuery.length() - 1));
        }

        viewDAO.createView(view);
    }

    @Override
    public void dropView(String schema, String view, UserConnection connection)
    {
        ViewDAO viewDAO = new ViewDAO(connection);

        viewDAO.deleteView(schema, view);
    }
}
