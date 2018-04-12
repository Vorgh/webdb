package rest.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rest.auth.ClientConnectionDetailsService;
import rest.exception.DatabaseConnectionException;
import rest.exception.MissingAuthInfoException;
import rest.model.connection.ConnectionAuthInfo;
import rest.model.connection.UserConnection;
import rest.util.UriUtils;

import java.util.*;

@Repository
public class ConnectionDAO
{
    private static final Logger logger = LoggerFactory.getLogger(ConnectionDAO.class);

    private BCryptPasswordEncoder passwordEncoder;
    private Map<String, UserConnection> connectedUsers;
    private ClientConnectionDetailsService clientDetailsService;

    @Autowired
    public ConnectionDAO(BCryptPasswordEncoder passwordEncoder,
                  ClientConnectionDetailsService clientDetailsService,
                  Map<String, UserConnection> connectedUsers)
    {
        this.passwordEncoder = passwordEncoder;
        this.clientDetailsService = clientDetailsService;
        this.connectedUsers = connectedUsers;
    }

    public void setAuthInfo(ConnectionAuthInfo connAuth) throws IllegalArgumentException, IllegalStateException
    {
        String url = connAuth.getUrl();
        String username = connAuth.getUsername();
        String password = connAuth.getPassword();

        if (url == null || username == null || url.equals("") || username.equals(""))
        {
            throw new MissingAuthInfoException("Missing url or username.");
        }

        /*Properties props = new Properties();
        props.setProperty("rewriteBatchedStatements", "true");
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setConnectionProperties(props);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);

        verifyConnection(jdbcTemplate);*/

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        hikariConfig.setConnectionTestQuery("SELECT 1");

        hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");
        hikariConfig.addDataSourceProperty("dataSource.rewriteBatchedStatements", "true");

        try
        {
            HikariDataSource ds = new HikariDataSource(hikariConfig);
            UserConnection userConnection = new UserConnection(url, username, passwordEncoder.encode(password), createUserAuthorities());
            userConnection.setJdbcTemplate(ds);
            connectedUsers.put(userConnection.getUrlUsernameID(), userConnection);
            clientDetailsService.addClient(createClientDetails(userConnection.getUsername(), password, userConnection.getAuthorities()));
        }
        catch (Exception e)
        {
            throw new DatabaseConnectionException("Couldn't access the database. Maybe wrong credentials?");
        }
    }

    @Transactional
    protected boolean verifyConnection(JdbcTemplate jdbcTemplate)
    {
        String sql = "SELECT 1";
        try
        {
            jdbcTemplate.execute(sql);
            return true;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            throw new DatabaseConnectionException("Couldn't access the database. Maybe wrong credentials?");
        }

    }

    public UserConnection getConnectedUserByDomainAndName(String url, String username)
    {
        String key = username+"@"+ UriUtils.extractDomainAndPortFromUrl(url);
        return connectedUsers.get(key);
    }

    private ClientDetails createClientDetails(String username, String password, Collection<? extends GrantedAuthority> authorities)
    {
        BaseClientDetails details = new BaseClientDetails();
        details.setClientId(username);
        details.setClientSecret(password);
        details.setAuthorizedGrantTypes(Arrays.asList("client_credentials", "password", "refresh_token") );
        details.setScope(Arrays.asList("read", "write", "trust"));
        details.setResourceIds(Arrays.asList("oauth2-resource"));
        details.setAuthorities(authorities);

        return details;
    }

    private Set<GrantedAuthority> createUserAuthorities()
    {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return authorities;
    }
}
