package rest.model.connection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserConnection extends User
{
    private String url;
    private Collection<? extends GrantedAuthority> authorities;
    private String urlUsernameID;
    private JdbcTemplate jdbcTemplate;

    public UserConnection(String username, String password, Collection<? extends GrantedAuthority> authorities)
    {
        super(username, password, authorities);
    }

    public UserConnection(String url, String username, String password, Collection<? extends GrantedAuthority> authorities)
    {
        super(username, password, authorities);

        if (url != null && !url.equals(""))
        {
            this.url = url;
            this.jdbcTemplate = jdbcTemplateBuilder(url, username, password);
            this.urlUsernameID = username+"@"+url;
        }
        else
            throw new IllegalArgumentException("Invalid null argument.");
    }

    private JdbcTemplate jdbcTemplateBuilder(String url, String username, String password)
    {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);

        return new JdbcTemplate(ds);
    }

    public String getUrl()
    {
        return url;
    }

    public String getUrlUsernameID()
    {
        return urlUsernameID;
    }

    public JdbcTemplate getJdbcTemplate()
    {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(String url, String username, String password)
    {
        this.jdbcTemplate = jdbcTemplateBuilder(url, username, password);
    }
}
