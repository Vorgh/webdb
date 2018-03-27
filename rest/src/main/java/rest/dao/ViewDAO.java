package rest.dao;

import rest.mapper.ViewMapper;
import rest.model.connection.UserConnection;
import rest.model.database.View;
import rest.model.request.Change;
import rest.sql.executor.ViewQueryExecutor;

import java.util.List;

public class ViewDAO extends AbstractDatabaseDAO
{
    private ViewQueryExecutor queryExecutor;

    public ViewDAO(UserConnection connection)
    {
        super(connection);
        this.queryExecutor = new ViewQueryExecutor(connection.getJdbcTemplate());
    }

    public List<View> getAllViewsMetadata(String schemaName)
    {
        return jdbcTemplate.query(
                "SELECT table_schema, table_name, view_definition " +
                        "FROM information_schema.views " +
                        "WHERE table_schema=?;",
                new Object[]{schemaName},
                new ViewMapper());
    }

    public View getViewMetadata(String schemaName, String viewName)
    {
        return jdbcTemplate.queryForObject(
                "SELECT table_schema, table_name, view_definition " +
                        "FROM information_schema.views " +
                        "WHERE table_schema=? AND table_name=?;",
                new Object[]{schemaName, viewName},
                new ViewMapper());
    }

    public void createView(View view)
    {
        this.queryExecutor.create(view);
    }

    public void alterView(Change<View> change)
    {
        this.queryExecutor.modify(change);
    }

    public void deleteView(String schema, String view)
    {
        this.queryExecutor.delete(schema, view);
    }
}
