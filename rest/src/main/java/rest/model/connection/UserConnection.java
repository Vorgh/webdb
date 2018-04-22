package rest.model.connection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import rest.util.UriUtils;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Properties;

public class UserConnection extends User
{
    private String url;
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
            this.urlUsernameID = username+"@"+ UriUtils.extractDomainAndPortFromUrl(url);
        }
        else
            throw new IllegalArgumentException("Invalid null argument.");
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

    public void setJdbcTemplate(DataSource ds)
    {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }
}
