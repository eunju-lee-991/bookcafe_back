package cafe_in.cafe_in.service;

import cafe_in.cafe_in.controller.JwtTokenVerifier;
import cafe_in.cafe_in.controller.constant.JwtTokenConstants;
import cafe_in.cafe_in.domain.Jwt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Slf4j
@Service
public class JwtTokenService {
    private int accessTokenExpMinutes = 10;
    private int refreshTokenExpMinutes = 100;
    private JwtTokenVerifier jwtTokenVerifier;

    // 토큰 발급
    public Jwt createToken(Long id, String nickname, String kakaoAccessToken, HttpServletResponse response) {
        Date accessTokenExp = new Date(System.currentTimeMillis() + (60000 * accessTokenExpMinutes));
        Date refreshTokenExp = new Date(System.currentTimeMillis() + (60000 * refreshTokenExpMinutes));

        String accessToken = JWT.create()
                .withSubject(String.valueOf(id))
                .withClaim("nickname", nickname)
                .withClaim("kakaoAccessToken", kakaoAccessToken)
                .withExpiresAt(accessTokenExp)
                .sign(Algorithm.HMAC512(JwtTokenConstants.SECRET_KEY));

        String refreshToken = JWT.create()
                .withSubject(String.valueOf(id))
                .withClaim("nickname", nickname)
                .withClaim("kakaoAccessToken", kakaoAccessToken)
                .withExpiresAt(refreshTokenExp)
                .sign(Algorithm.HMAC512(JwtTokenConstants.SECRET_KEY));

        return Jwt.builder().
                accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExp(accessTokenExp)
                .refreshTokenExp(refreshTokenExp)
                .build();
    }

    // 토큰 정보확인
    public Jwt getTokenInfo(String receivedToken) {
        String accessToken = null;
        Date accessTokenExp = null;
        Long id = null;
        String socialAccessToken = null;
        jwtTokenVerifier = new JwtTokenVerifier(JwtTokenConstants.SECRET_KEY);

        DecodedJWT decodedJWT = jwtTokenVerifier.verify(receivedToken); // = JWT.require(Algorithm.HMAC512(JwtTokenConstants.SECRET_KEY)).build().verify(receivedToken);

        if (decodedJWT.getClaim("nickname") != null) {
            accessToken = receivedToken;
            accessTokenExp = decodedJWT.getExpiresAt();
            id = Long.valueOf(decodedJWT.getSubject());
            socialAccessToken = decodedJWT.getClaim("kakaoAccessToken").asString();

            log.info(accessTokenExp.toString());
        } else {
            throw new RuntimeException("invalid JWT access token.");
        }
        return Jwt.builder().accessToken(accessToken).accessTokenExp(accessTokenExp).loginId(id).socialAccessToken(socialAccessToken).build();
    }

    // 토큰 갱신
    public Jwt refreshToken(String refreshTokenCookie) {
        // 리프레쉬 토큰 검증

        // 액세스 토큰 새로 발급

        // 액세스토큰, 만료기한 리턴
        return null;
    }
}
