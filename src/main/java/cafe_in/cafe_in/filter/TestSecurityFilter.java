package cafe_in.cafe_in.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TestSecurityFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("doFilterdoFilterdoFilterdoFiltersd");
        HttpServletRequest req = (HttpServletRequest) request;
        String url = req.getRequestURL().toString();
        System.out.println(url);
        super.doFilter(request, response, chain);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Authentication authentication = null;
        System.out.println("attemptAuthenticationattemptAuthenticationattemptAuthenticationattemptAuthenticationattemptAuthentication");
        return authentication;
    }

}
