package rest.auth.filter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.web.filter.GenericFilterBean;
import rest.util.ResettableStreamHttpServletRequest;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthFilter extends GenericFilterBean
{
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        ResettableStreamHttpServletRequest req = new ResettableStreamHttpServletRequest((HttpServletRequest)servletRequest);
        HttpServletResponse resp = (HttpServletResponse)servletResponse;

        String rawBody = IOUtils.toString(req.getReader());
        Map<String, String> body = Arrays.stream(rawBody.replaceAll("[{}\n\"]", "").split(","))
                .collect(Collectors.toMap((String p) -> p.substring(0, p.indexOf(":")), p -> p.substring(p.indexOf(":") + 1)));
        String authString = body.get("username") + ":" + body.get("password");

        Cookie testCookie = new Cookie("auth_id", Base64.encodeBase64String(authString.getBytes()));
        testCookie.setPath("/");
        resp.addCookie(testCookie);

        req.resetInputStream();

        filterChain.doFilter(req, resp);
    }
}
