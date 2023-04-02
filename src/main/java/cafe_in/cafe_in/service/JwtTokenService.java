package cafe_in.cafe_in.service;

import cafe_in.cafe_in.controller.JwtTokenVerifier;
import cafe_in.cafe_in.controller.constant.JwtTokenConstants;
import cafe_in.cafe_in.domain.Jwt;
import cafe_in.cafe_in.exception.InvalidJwtTokenException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class JwtTokenService {
    private int accessTokenExpMinutes = 10;
    private int refreshTokenExpMinutes = 100;
    private JwtTokenVerifier jwtTokenVerifier;

    public Jwt createTokens(Long id, String nickname, String socialAccessToken) {
        String accessToken = createToken(id, nickname, socialAccessToken, accessTokenExpMinutes, "accessToken");
        String refreshToken = createToken(id, nickname, socialAccessToken, refreshTokenExpMinutes, "refreshToken");

        return Jwt.builder().
                accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExp(new Date(System.currentTimeMillis() + 60000 * accessTokenExpMinutes))
                .refreshTokenExp(new Date(System.currentTimeMillis() + 60000 * refreshTokenExpMinutes))
                .build();
    }

    // 토큰 정보확인
    public Jwt getAccessTokenInfo(String receivedToken) {
        String accessToken = null;
        Date accessTokenExp = null;
        String socialAccessToken = null;
        Long id = null;

        try {
            jwtTokenVerifier = new JwtTokenVerifier(JwtTokenConstants.SECRET_KEY);
            DecodedJWT decodedJWT = jwtTokenVerifier.verify(receivedToken); // = JWT.require(Algorithm.HMAC512(JwtTokenConstants.SECRET_KEY)).build().verify(receivedToken);

            if (decodedJWT.getClaim("nickname") != null) {
                accessToken = receivedToken;
                accessTokenExp = decodedJWT.getExpiresAt();
                id = Long.valueOf(decodedJWT.getSubject());
                socialAccessToken = decodedJWT.getClaim("socialAccessToken").asString();
            } else {
                throw new InvalidJwtTokenException(receivedToken);
            }
        }catch (JWTVerificationException ex) {
            throw new InvalidJwtTokenException(receivedToken);
        }

        return Jwt.builder().accessToken(accessToken).accessTokenExp(accessTokenExp).loginId(id).socialAccessToken(socialAccessToken).build();
    }

    // 토큰 갱신
    public Jwt refreshAccessToken(String refreshToken) {
        String accessToken = null;
        jwtTokenVerifier = new JwtTokenVerifier(JwtTokenConstants.SECRET_KEY);
        DecodedJWT decodedJWT = jwtTokenVerifier.verify(refreshToken);

        // 리프레쉬 토큰 검증 후 액세스 토큰 새로 발급
        if (decodedJWT.getClaim("nickname") != null && decodedJWT.getClaim("tokenType").asString().equals("refreshToken")) {
            accessToken = createToken(Long.valueOf(decodedJWT.getSubject()), decodedJWT.getClaim("nickname").asString()
                    , decodedJWT.getClaim("socialAccessToken").asString(), accessTokenExpMinutes, "accessToken");
        } else {
            throw new InvalidJwtTokenException(refreshToken);
        }

        return Jwt.builder().accessToken(accessToken)
                .accessTokenExp(new Date(System.currentTimeMillis() + 60000 * accessTokenExpMinutes)).build();
    }

    // 토큰 발급
    public String createToken(Long id, String nickname, String socialAccessToken, int expMinutes, String tokenType) {
        Date accessTokenExp = new Date(System.currentTimeMillis() + (60000 * expMinutes));

        String createdToken = JWT.create()
                .withSubject(String.valueOf(id))
                .withClaim("nickname", nickname)
                .withClaim("socialAccessToken", socialAccessToken)
                .withClaim("tokenType", tokenType)
                .withExpiresAt(accessTokenExp)
                .sign(Algorithm.HMAC512(JwtTokenConstants.SECRET_KEY));

        return createdToken;
    }
}
