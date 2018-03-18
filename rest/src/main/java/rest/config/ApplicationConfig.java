package rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import rest.model.connection.UserConnection;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApplicationConfig
{
    @Bean
    public Map<String, UserConnection> connectedUsers()
    {
        return new HashMap<>();
    }

    @Bean
    public DriverManagerDataSource dataSource()
    {
        return new DriverManagerDataSource();
    }
}
