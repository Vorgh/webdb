package rest.model.connection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserConnection implements UserDetails
{
    private String url;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private String urlUsernameID;

    public UserConnection(String url, String username, String password)
    {
        if (url != null && !url.equals("") && username != null && !username.equals(""))
        {
            this.url = url;
            this.username = username;
            this.password = password;

            this.urlUsernameID = url + "_*_" + username;
        }
        else
            throw new IllegalArgumentException("Url can't be null.");
    }

    public String getUrl()
    {
        return url;
    }

    public String getUrlUsernameID()
    {
        return urlUsernameID;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return false;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }
}
