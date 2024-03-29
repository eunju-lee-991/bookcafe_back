package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.controller.constant.JwtTokenConstants;
import cafe_in.cafe_in.controller.constant.KakaoApiConstants;
import cafe_in.cafe_in.controller.constant.KakaoApiKey;
import cafe_in.cafe_in.controller.constant.SessionConstants;
import cafe_in.cafe_in.domain.Jwt;
import cafe_in.cafe_in.domain.Member;
import cafe_in.cafe_in.dto.jwt.CreateTokenResponse;
import cafe_in.cafe_in.dto.jwt.RefreshTokenResponse;
import cafe_in.cafe_in.dto.member.LoginMemberResponse;
import cafe_in.cafe_in.dto.member.MemberDto;
import cafe_in.cafe_in.exception.AuthorizationCodeNotFoundException;
import cafe_in.cafe_in.exception.InvalidTokenException;
import cafe_in.cafe_in.service.JwtTokenService;
import cafe_in.cafe_in.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static cafe_in.cafe_in.common.Common.setCookie;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/*")
public class LoginController {

    private final MemberService memberService;
    private final JwtTokenService tokenService;

    /**
     * 토큰 만료 및 refresh는 나중에 처리
     */
    private String refresh_token;
    private int expires_in;
    private int refresh_token_expires_in;

    @GetMapping("/jwt-test")
    public RefreshTokenResponse testJWT(HttpServletRequest request, HttpServletResponse response) {
        RefreshTokenResponse refreshTokenResponse = null;
        Member member = (Member) request.getSession(false).getAttribute("MEMBER");

        String authHeader = request.getHeader("Authorization");
        log.info(authHeader);

        Jwt tokenInfo = null;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            log.info("authHeader 없음");
        } else {
            tokenInfo = tokenService.getAccessTokenInfo(authHeader.replace(JwtTokenConstants.TOKEN_PREFIX, ""));
        }

        return refreshTokenResponse;
    }

    /**
     * 인가코드를 서버에서 바로 받으면 토큰 받고 회원가입/로그인 처리까지 한 후에 프론트엔드 서버로 다시 돌아가기 어려움
     * 그래서 client server에서 인가코드 받고 백엔드로 넘겨준 후에 백엔드에서 토큰 발급/회원가입or로그인 처리하였음
     */
    @GetMapping("/login/token")
    public LoginMemberResponse login(@RequestParam String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorize_code = "";
        String kakaoAccessToken = "";
        Member member = null;
        boolean newMember = false;

        if (code != null) {
            authorize_code = code;
        }

        // 카카오 액세스 토큰 받기
        kakaoAccessToken = getToken(authorize_code, response);
        // 토큰 유효성 검증
        validateToken(kakaoAccessToken);
        // 토큰으로 회원정보 조회
        KakaoMemberInfo memberInfo = getKakaoUserInfo(kakaoAccessToken);
        // 조회한 회원정보로 회원가입 여부 확인
        boolean isExist = memberService.isExistingMember(memberInfo.getKakaoMemberId());

        if (isExist) { // 회원이면 회원 조회
            member = memberService.findOne(memberInfo.getKakaoMemberId());
        } else { // 회원 아니면 회원가입
            member = join(memberInfo);
        }

        Jwt jwt = tokenService.createTokens(member.getId(), member.getNickname(), kakaoAccessToken); // 세션 저장 대신 JWT 토큰 사용

        // 리프레쉬 토큰 httpOnly 쿠키 설정
        long maxAge = (jwt.getRefreshTokenExp().getTime() - System.currentTimeMillis()) / 1000;
        Cookie refreshTokenCookie = setCookie("refreshToken", jwt.refreshToken, true, false, (int) maxAge, "/");
        response.addCookie(refreshTokenCookie);

        CreateTokenResponse createTokenResponse = new CreateTokenResponse(jwt.accessToken, jwt.refreshToken, jwt.accessTokenExp, jwt.refreshTokenExp);

        return new LoginMemberResponse(newMember, new MemberDto(member.getId(), member.getNickname(), member.getEmail(), member.getProfileImageUrl(), member.getJoinDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))), createTokenResponse);
    }

    private void login(HttpServletRequest request, HttpServletResponse response, Member member, String kakaoAccessToken) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.LOGIN_MEMBER, member);
        session.setAttribute(SessionConstants.ACCESS_TOKEN, kakaoAccessToken);
        log.info(session.getId());
    }

    @GetMapping("/logout")
    private ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies())
//                .filter(cookie -> cookie.getName().equals("refreshToken")).findFirst();
//
//        if (refreshTokenCookie.isPresent()) {
//            refreshTokenCookie.get().setMaxAge(0);
//            response.addCookie(refreshTokenCookie.get());
//        }

        response.addHeader("Set-Cookie", "refreshToken=; Max-Age=0; Path=/; HttpOnly");  // refreshTokenCookie 삭제. HttpOnly여서 서버에서 삭제

        String accessToken = request.getHeader(JwtTokenConstants.HEADER_AUTHORIZATION).replace(JwtTokenConstants.TOKEN_PREFIX, "");

        Jwt jwt = tokenService.getAccessTokenInfo(accessToken);
        Long loggedoutId = kakaoLogout(jwt.getSocialAccessToken()); // 카카오 로그아웃

        if (loggedoutId == null) {
            throw new RuntimeException("logout failed");
        }

        return ResponseEntity.noContent().build();
    }

    private Long kakaoLogout(String kakaoAccessToken) throws IOException {
        validateToken(kakaoAccessToken);

        Long loggedOutId = null;
        HttpURLConnection connection = getConnection(KakaoApiConstants.URLs.LOGOUT_URL, "POST", false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Authorization", "Bearer " + kakaoAccessToken);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String result = getResultString(connection.getInputStream());

            JsonParser jsonParser = new JacksonJsonParser();
            Map<String, Object> map = jsonParser.parseMap(result);
            loggedOutId = (Long) map.get("id");

        } else { // responseCode not HTTP_OK
            String result = getResultString(connection.getErrorStream());

            JsonParser jsonParser = new JacksonJsonParser();
            Map<String, Object> map = jsonParser.parseMap(result);
            int code = (int) map.get("code");
            String msg = (String) map.get("msg");
            log.info("error code : {}, msg : {} ", code, msg);

            throw new RuntimeException(msg);
        }

        return loggedOutId;
    }

    private Member join(KakaoMemberInfo memberInfo) {
        Member member = new Member();
        member.setId(memberInfo.getKakaoMemberId());
        member.setNickname(memberInfo.getNickname());
        member.setEmail(memberInfo.getEmail());
        member.setProfileImageUrl(memberInfo.getProfileImageUrl());
        memberService.join(member);

        return member;
    }

    private String getToken(String authorize_code, HttpServletResponse response) throws IOException {
        String kakaoAccessToken = "";
        HttpURLConnection connection = getConnection(KakaoApiConstants.URLs.GET_TOKEN_URL, "POST", true);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        StringBuilder sb = new StringBuilder();

        sb.append("grant_type=authorization_code");
        sb.append("&client_id=" + KakaoApiKey.REST_API_KEY);
        sb.append("&redirect_uri=" + KakaoApiConstants.URLs.REDIRECT_URI);
        sb.append("&code=" + authorize_code);
        bw.write(sb.toString());
        bw.flush();

        int responseCode = connection.getResponseCode();
        log.info("getToken response code : {}", responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String result = getResultString(connection.getInputStream());

            JsonParser jsonParser = new JacksonJsonParser();

            Map<String, Object> map = jsonParser.parseMap(result);
            kakaoAccessToken = (String) map.get("access_token");

            /**
             * 토큰 만료 및 refresh는 나중에 처리
             */
            refresh_token = (String) map.get("refresh_token");
//            String refreshToken = (String) map.get("refresh_token");
//            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
//            refreshTokenCookie.setMaxAge(1000000);
//            response.addCookie(refreshTokenCookie);

            expires_in = (int) map.get("expires_in");
            refresh_token_expires_in = (int) map.get("refresh_token_expires_in");

            log.info("token : {} ", kakaoAccessToken);
            log.info("expires_id  : {}", expires_in);

        } else {
            // 나중에 Error 처리 제대로!!!!!!!!!!!!
            String result = getResultString(connection.getErrorStream());

            JsonParser jsonParser = new JacksonJsonParser();
            /**
             * map에 error, error_description, error_code 들어있음
             */
            Map<String, Object> map = jsonParser.parseMap(result);
            String error = (String) map.get("error");
            String error_code = (String) map.get("error_code");
            String error_description = (String) map.get("error_description");

            log.error("error: {} ", error);
            log.error("error_code: {} ", error_code);
            log.error("error_Description: {} ", error_description);

            if (error_code.equals("KOE320")) { // authorize_code not found
                throw new AuthorizationCodeNotFoundException(error_description);
            } else if (error_code.equals("KOE303")) { // Redirect URI mismatch.
                throw new RuntimeException("Redirect URI mismatch");
            } else if (error_code.equals("KOE101")) { // Not exist client_id
                throw new RuntimeException("Not exist client_id");
            }
        }

        return kakaoAccessToken;
    }

    private boolean validateToken(String kakaoAccessToken) throws IOException {
        HttpURLConnection connection = getConnection(KakaoApiConstants.URLs.VALIDATE_TOKEN_URL, "GET", false);
        connection.setRequestProperty("Authorization", "Bearer " + kakaoAccessToken);

        int responseCode = connection.getResponseCode();
        log.info("responseCode" + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
            String result = getResultString(connection.getInputStream());

            JsonParser jsonParser = new JacksonJsonParser();
            Map<String, Object> map = jsonParser.parseMap(result);
            Long id = (Long) map.get("id");
            Integer expires_in = (Integer) map.get("expires_in");

            log.info("id : {}, expires_in : {} ", id, expires_in);

            return true;
        } else { // not 200
            String result = getResultString(connection.getErrorStream());

            JsonParser jsonParser = new JacksonJsonParser();
            Map<String, Object> map = jsonParser.parseMap(result);
            int code = (int) map.get("code");
            String msg = (String) map.get("msg");

            log.info("code : {}, msg : {} ", code, msg);

            if (code == -401) {
                throw new InvalidTokenException(msg, kakaoAccessToken);
            } else {
                throw new RuntimeException(msg);
            }
        }
    }

    private KakaoMemberInfo getKakaoUserInfo(String kakaoAccessToken) throws IOException {
        KakaoMemberInfo kakaoMemberInfo = null;

        String[] propertyKeys = {"kakao_account.profile", "kakao_account.name", "kakao_account.email", "kakao_account.age_range", "kakao_account.birthday", "kakao_account.gender"};
        // propertyKeys 배열 형식의 스트링으로 변환
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < propertyKeys.length; i++) {
            sb.append("\"" + propertyKeys[i] + "\"");
            if (i < propertyKeys.length - 1) {
                sb.append(",");
            } else {
                sb.append("]");
            }
        }

        String query = "?property_keys=" + sb.toString();
        HttpURLConnection connection = getConnection(KakaoApiConstants.URLs.GET_USER_INFO + query, "GET", false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        connection.setRequestProperty("Authorization", "Bearer " + kakaoAccessToken);

        int responseCode = connection.getResponseCode();
        log.info("회원조회 response code : {}", responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String result = getResultString(connection.getInputStream());

            JsonParser jsonParser = new JacksonJsonParser();
            Map<String, Object> map = jsonParser.parseMap(result);
            Long id = (Long) map.get("id");

            Map<String, Object> kakaoAccount = (Map<String, Object>) map.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            String nickname = (String) profile.get("nickname");
            String email = (String) kakaoAccount.get("email");
            String profileImageUrl = (String) profile.get("profile_image_url");

            kakaoMemberInfo = new KakaoMemberInfo();
            kakaoMemberInfo.setKakaoMemberId(id);
            kakaoMemberInfo.setNickname(nickname);
            kakaoMemberInfo.setEmail(email);
            kakaoMemberInfo.setProfileImageUrl(profileImageUrl);
        }

        return kakaoMemberInfo;
    }

    private String getResultString(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        StringBuilder result = new StringBuilder();

        while ((line = br.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    private HttpURLConnection getConnection(String string_url, String requestMethod, boolean doOutPut) throws IOException {
        URL url = new URL(string_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.setDoOutput(doOutPut);

        return connection;
    }

    @Getter
    @Setter
    class KakaoMemberInfo {
        private Long kakaoMemberId;
        private String nickname;
        private String email;
        private String profileImageUrl;
    }
}
