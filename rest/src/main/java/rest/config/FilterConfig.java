package rest.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rest.auth.filter.AuthFilter;

@Configuration
public class FilterConfig
{
    /*@Bean
    public FilterRegistrationBean AuthFilterRegistrationBean() {
        FilterRegistrationBean regBean = new FilterRegistrationBean();
        regBean.setFilter(new AuthFilter());
        regBean.setOrder(999);
        regBean.addUrlPatterns("/connection/connectionAuth");

        return regBean;
    }*/
}
