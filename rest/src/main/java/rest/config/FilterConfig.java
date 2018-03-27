package rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rest.auth.PostTokenFilter;

@Configuration
public class FilterConfig
{
    /*@Bean
    public FilterRegistrationBean PostTokenFilterRegistrationBean() {
        FilterRegistrationBean regBean = new FilterRegistrationBean();
        regBean.setFilter(new PostTokenFilter());
        regBean.setOrder(999);
        regBean.addUrlPatterns("/oauth/token");

        return regBean;
    }*/
}
