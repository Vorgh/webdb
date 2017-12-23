package rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DriverConfiguration
{
    @Bean
    public DriverManagerDataSource dataSource()
    {
        return new DriverManagerDataSource();
    }
}

