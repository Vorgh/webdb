package rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rest.model.connection.UserConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ConnectedUserListConfig
{
    @Bean
    public Map<String, UserConnection> connectedUsers()
    {
        return new HashMap<>();
    }
}
