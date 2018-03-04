package rest.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import rest.model.connection.UserConnection;

public abstract class AbstractDatabaseDAO
{
    private UserConnection connection;
    JdbcTemplate jdbcTemplate;

    AbstractDatabaseDAO(UserConnection connection)
    {
        this.connection = connection;
        this.jdbcTemplate = connection.getJdbcTemplate();
    }

    public UserConnection getConnection()
    {
        return connection;
    }

    public void setConnection(UserConnection connection)
    {
        this.connection = connection;
        this.jdbcTemplate = connection.getJdbcTemplate();
    }

    String quote(String s)
    {
        return "`" + s + "`";
    }

    String quoteValue(String s)
    {
        return "\"" + s + "\"";
    }
}
