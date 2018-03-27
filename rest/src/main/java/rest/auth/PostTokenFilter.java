package rest.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PostTokenFilter extends GenericFilterBean
{
    private TokenStore tokenStore;

    @Override
    protected void initFilterBean() throws ServletException
    {
        WebApplicationContext webApplicationContext =
                WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        //reference to bean from app context
        tokenStore = webApplicationContext.getBean(TokenStore.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;

        String authHeader = req.getHeader("Authorization");
        if (!"".equals(authHeader) && authHeader.startsWith("Basic"))
        {
            System.out.println(authHeader);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof OAuth2Authentication)
        {
            OAuth2Authentication oAuth = (OAuth2Authentication)auth;
            Cookie testCookie = new Cookie("access_token", tokenStore.getAccessToken(oAuth).getValue());
            testCookie.setHttpOnly(true);
            testCookie.setPath("/");
            resp.addCookie(testCookie);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
