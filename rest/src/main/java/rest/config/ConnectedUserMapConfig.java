package rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rest.model.connection.UserConnection;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConnectedUserMapConfig
{
    @Bean
    public Map<String, UserConnection> connectedUsers()
    {
        return new HashMap<>();
    }
}
