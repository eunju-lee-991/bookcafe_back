package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.domain.Jwt;
import cafe_in.cafe_in.dto.jwt.RefreshTokenResponse;
import cafe_in.cafe_in.exception.InvalidJwtTokenException;
import cafe_in.cafe_in.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class JwtTokenController {
    private final JwtTokenService tokenService;

    // 토큰 갱신
    @GetMapping("/api/jwt/refresh")
    public ResponseEntity testJWT(HttpServletRequest request, HttpServletResponse response) {
        RefreshTokenResponse refreshTokenResponse = null;

        Optional<Cookie> refreshToken = Arrays.stream(request.getCookies()).filter(
                cookie -> cookie.getName().equals("refreshToken")
        ).findFirst();

        if (refreshToken.isPresent()) {
            Jwt jwt = tokenService.refreshAccessToken(refreshToken.get().getValue());
            refreshTokenResponse = RefreshTokenResponse.builder().accessToken(jwt.getAccessToken())
                    .accessTokenExp(jwt.getAccessTokenExp()).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(refreshTokenResponse);
    }
}
