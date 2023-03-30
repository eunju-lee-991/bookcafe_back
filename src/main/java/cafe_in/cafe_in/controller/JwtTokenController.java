package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtTokenController {
    private final JwtTokenService tokenService;

    // 토큰 발급

    // 토큰 갱신

}
