package rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import rest.auth.RestAuthenticationEntryPoint;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter
{
    private RestAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public ResourceServerConfig(RestAuthenticationEntryPoint authenticationEntryPoint)
    {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http
            .authorizeRequests()
            .antMatchers("/connection/**").permitAll()
            .antMatchers("/oauth/**").permitAll()
            .antMatchers("/db/**").authenticated()
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }
}
