package rest.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import rest.model.connection.UserConnection;

import javax.sql.DataSource;
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
    public DataSource dataSource(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306");
        hikariConfig.setUsername("test");
        hikariConfig.setPassword("test");

        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName("springHikariCP");

        hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");
        hikariConfig.addDataSourceProperty("dataSource.rewriteBatchedStatements", "true");

        return new HikariDataSource(hikariConfig);
    }
}
