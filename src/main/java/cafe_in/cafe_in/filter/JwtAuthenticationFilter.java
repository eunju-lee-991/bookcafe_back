package cafe_in.cafe_in.filter;

import cafe_in.cafe_in.controller.JwtTokenVerifier;
import cafe_in.cafe_in.controller.constant.JwtTokenConstants;
import cafe_in.cafe_in.domain.Member;
import cafe_in.cafe_in.repository.member.MemberRepository;
import cafe_in.cafe_in.repository.member.MemberRepositoryImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWTVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtTokenVerifier jwtTokenVerifier;
    private final MemberRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(JwtTokenConstants.HEADER_AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(JwtTokenConstants.TOKEN_PREFIX)) {
            String accessToken = authHeader.replace(JwtTokenConstants.TOKEN_PREFIX, "");

            try {
                jwtTokenVerifier = new JwtTokenVerifier(JwtTokenConstants.SECRET_KEY);
                DecodedJWT decodedJWT = jwtTokenVerifier.verify(accessToken);

                Long id = Long.parseLong(decodedJWT.getSubject());
                Optional<Member> member = repository.findOne(id);

                if (member.isPresent()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("MEMBER", member.get());
                }

                filterChain.doFilter(request, response);

            }catch (JWTVerificationException ex) {
                log.error(ex.getMessage());
            }
        }else {
            throw new RuntimeException("AUTHORIZATION 헤더가 이상해요");
        }
    }
}
