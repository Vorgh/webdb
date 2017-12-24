package rest.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import rest.model.ConnectionAuthInfo;
import rest.model.UserConnection;

import java.sql.SQLException;
import java.util.*;

@Repository
public class ConnectionDAOImpl implements ConnectionDAO
{
    private JdbcTemplate jdbcTemplate;
    private BCryptPasswordEncoder passwordEncoder;
    private List<UserConnection> connectionList;
    private ClientConnectionDetailsService clientDetailsService;

    @Autowired
    ConnectionDAOImpl(JdbcTemplate jdbcTemplate, BCryptPasswordEncoder passwordEncoder, ClientConnectionDetailsService clientDetailsService)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
        this.clientDetailsService = clientDetailsService;
        this.connectionList = new ArrayList<>();
    }

    public void setAuthInfo(ConnectionAuthInfo connAuth) throws IllegalArgumentException, IllegalStateException
    {
        DriverManagerDataSource ds = (DriverManagerDataSource)jdbcTemplate.getDataSource();

        String originalUrl = ds.getUrl();
        String originalUsername = ds.getUsername();
        String originalPassword = ds.getPassword();

        String url = connAuth.getUrl();
        String username = connAuth.getUsername();
        String password = connAuth.getPassword();

        if (url == null || username == null || url.equals("") || username.equals(""))
        {
            throw new IllegalArgumentException("Missing url or username.");
        }

        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);

        if (!testConnection())
        {
            ds.setUrl(originalUrl);
            ds.setUsername(originalUsername);
            ds.setPassword(originalPassword);
            throw new IllegalStateException("Couldn't access the database.");
        }


        //connAuth.setPassword(passwordEncoder.encode(password));

        UserConnection userConnection = new UserConnection(url, username, passwordEncoder.encode(password));
        connectionList.add(userConnection);
        clientDetailsService.addClient(createClientDetails(userConnection.getUsername(), password));
    }

    @Transactional
    public boolean testConnection()
    {
        String sql = "SELECT 1";
        //RowMapper<Article> rowMapper = new BeanPropertyRowMapper<Article>(Article.class);
        try
        {
            this.jdbcTemplate.execute(sql);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }

    public UserConnection getConnectedUserByName(String username)
    {
        return connectionList.stream()
                .filter(uc -> uc.getUsername().equals(username))
                .findFirst()
                .get();
    }

    public void logout(UserConnection userConnection)
    {
        connectionList.remove(userConnection);
    }

    private ClientDetails createClientDetails(String username, String password)
    {
        BaseClientDetails details = new BaseClientDetails();
        details.setClientId(username);
        details.setClientSecret(password);
        details.setAuthorizedGrantTypes(Arrays.asList("client_credentials", "password") );
        details.setScope(Arrays.asList("read", "write", "trust"));
        details.setResourceIds(Arrays.asList("oauth2-resource"));
        details.setAccessTokenValiditySeconds(3600);
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        details.setAuthorities(authorities);

        return details;
    }
}
