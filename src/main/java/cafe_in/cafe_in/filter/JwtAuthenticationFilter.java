package cafe_in.cafe_in.filter;

import cafe_in.cafe_in.controller.JwtTokenVerifier;
import cafe_in.cafe_in.controller.constant.JwtTokenConstants;
import cafe_in.cafe_in.domain.Member;
import cafe_in.cafe_in.repository.member.MemberRepository;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private JwtTokenVerifier jwtTokenVerifier;
    private MemberRepository repository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, MemberRepository repository) {
        super(authenticationManager);
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(JwtTokenConstants.HEADER_AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(JwtTokenConstants.TOKEN_PREFIX)) {
            String accessToken = authHeader.replace(JwtTokenConstants.TOKEN_PREFIX, "");

            try {
                jwtTokenVerifier = new JwtTokenVerifier(JwtTokenConstants.SECRET_KEY);
                DecodedJWT decodedJWT = jwtTokenVerifier.verify(accessToken);
                Long id = Long.parseLong(decodedJWT.getSubject());

                Authentication authentication = null;

                if (id != null) {
                    Optional<Member> member = repository.findOne(id);

                    if (member.isPresent())
                        authentication = new UsernamePasswordAuthenticationToken(id, null, Collections.emptyList());
                }

                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);

            } catch (JWTVerificationException ex) {
                setUnthorizedResponse(response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    void setUnthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // HTTP 응답 상태 코드 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String message = "{\"message\": \"Access Token is invalid. Unauthorized\"}";
        response.getWriter().write(message);
        response.getWriter().flush();
    }
}
