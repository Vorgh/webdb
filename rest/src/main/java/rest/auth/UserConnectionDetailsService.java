package rest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rest.dao.ConnectionDAO;
import rest.model.connection.UserConnection;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserConnectionDetailsService implements UserDetailsService
{
    @Autowired
    private ConnectionDAO connectionDAO;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        UserConnection user = connectionDAO.getConnectedUserByName(s);
        if (user == null)
        {
            throw new UsernameNotFoundException(s);
        }

        return new User(user.getUsername(), user.getPassword(), generateAuthorities());
    }

    private List<GrantedAuthority> generateAuthorities()
    {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return authorities;
    }
}
