package cafe_in.cafe_in.controller.Login;

import cafe_in.cafe_in.domain.Member;
import cafe_in.cafe_in.exception.AuthorizationCodeNotFoundException;
import cafe_in.cafe_in.service.MemberService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    /**
     * 토큰 만료 및 refresh는 나중에 처리
     */
    private String refresh_token;
    private int expires_in;
    private int refresh_token_expires_in;


    /**
     * 인가코드를 서버에서 바로 받으면 토큰 받고 회원가입/로그인 처리까지 한 후에 프론트엔드 서버로 다시 돌아가기 어려움
     * 그래서 client server에서 인가코드 받고 백엔드로 넘겨준 후에 백엔드에서 토큰 발급/회원가입or로그인 처리하였음
     */
    @GetMapping("/api/login/token")
    public Result login(@RequestParam String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorize_code = "";
        String access_token = "";
        Member member = null; // REST API 답게 리턴하도록 수정
        boolean isNewMember = false;

        if (code != null) {
            authorize_code = code;
            log.info(authorize_code);
        }

        // 토큰 받기
        access_token = getToken(authorize_code);

        // 토큰 유효성 검증
        validateToken(access_token);

        // 토큰으로 회원정보 조회
        KakaoMemberInfo memberInfo = getKakaoUserInfo(access_token);

        // 조회한 회원정보로 회원가입 여부 확인
        boolean isExist = memberService.isExistingMember(memberInfo.getKakaoMemberId());

        if (isExist) {
            // 회원이면 회원 조회
            member = memberService.findOne(memberInfo.getKakaoMemberId());
            log.info("로그인");
        } else {
            // 회원 아니면 회원가입
            member = join(memberInfo);
            isNewMember = true;
            log.info("회원가입");
        }

        // 로그인 처리(세션에 토큰이랑 회원정보 저장 후 쿠키)
        login(request, response, member, access_token);

        return new Result(isNewMember, member);
    }

    @GetMapping("/testCookie")
    public void testCookie(HttpServletRequest request) {
        HttpSession session = request.getSession();
        boolean isValidJsessionid = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("JSESSIONID")).findFirst().get().getValue().equals(session.getId());
        if (isValidJsessionid) {
            String jsessionid1 = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("JSESSIONID")).findFirst().get().getValue();
            log.info(jsessionid1);
            log.info((String) session.getAttribute(SessionConstants.ACCESS_TOKEN));
            Member member = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);
            log.info(member.getEmail());
        } else {
            String jsessionid2 = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("JSESSIONID")).findFirst().get().getValue();
            log.info("2222222" + jsessionid2);
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies).forEach(cookie -> System.out.println(cookie.getName()));
        } else {
            System.out.println("no cookies");
        }

    }

    private void login(HttpServletRequest request, HttpServletResponse response, Member member, String access_token) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.LOGIN_MEMBER, member);
        session.setAttribute(SessionConstants.ACCESS_TOKEN, access_token);
        log.info(session.getId());
    }


    @GetMapping("/logout")
    private String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        Cookie[] cookies = request.getCookies();
        boolean isValidJsessionid = false;

        if (cookies != null && session != null) {
            Optional<Cookie> jsessionCookie = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("JSESSIONID")).findFirst();
            if (jsessionCookie.isPresent()) {
                Cookie jCookie = jsessionCookie.get();
                isValidJsessionid = jCookie.getValue().equals(session.getId()); // check if the Jsessionid cookie value equals session id

                if (isValidJsessionid) {
                    String access_token = (String) session.getAttribute(SessionConstants.ACCESS_TOKEN);
                    // kakao Logout
                    Long logout = kakaoLogout(access_token);
                    // delete session
                    session.invalidate();
                    // delete JSESSIONID cookie

                } else { // not valid JSESSIONID
                    // ..Exception or Error
                }
                // JSESSIONID는 HttpOnly여서 cliend side에서 수정x 서버에서 지워줘야함
                jCookie.setMaxAge(0);
                response.addCookie(jCookie);
            }
        } else {
            log.info("cookie or session is null");
        }
        return "카카오 로그아웃 및 세션 삭제 완료. 리턴값 나중에 수정";
    }

    private Long kakaoLogout(String access_token) throws IOException {
        Long loggedOutId = null;

        HttpURLConnection connection = getConnection(KakaoApiConstants.URLs.LOGOUT_URL, "POST", false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Authorization", "Bearer " + access_token);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String result = getResultString(connection.getInputStream());

            JsonParser jsonParser = new JacksonJsonParser();
            Map<String, Object> map = jsonParser.parseMap(result);
            loggedOutId = (Long) map.get("id");

            log.info("LOGOUT id : {}", loggedOutId);
        } else { // responseCode not HTTP_OK
            String result = getResultString(connection.getErrorStream());

            JsonParser jsonParser = new JacksonJsonParser();
            Map<String, Object> map = jsonParser.parseMap(result);
            int code = (int) map.get("code");
            String msg = (String) map.get("msg");

            log.info("code : {}, msg : {} ", code, msg);

            // exception 만들어서 처리!!!!!!!!!!!! NotValidTokenException

            throw new RuntimeException(msg);

        }

        return loggedOutId;
    }

    private Member join(KakaoMemberInfo memberInfo) {
        Member member = new Member();
        member.setId(memberInfo.getKakaoMemberId());
        member.setNickname(memberInfo.getNickname());
        member.setEmail(memberInfo.getEmail());
        memberService.join(member);

        return member;
    }


    private String getToken(String authorize_code) throws IOException {
        String access_token = "";
        HttpURLConnection connection = getConnection(KakaoApiConstants.URLs.GET_TOKEN_URL, "POST", true);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        StringBuilder sb = new StringBuilder();

        sb.append("grant_type=authorization_code");
        sb.append("&client_id=" + KakaoApiConstants.REST_API_KEY);
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
            access_token = (String) map.get("access_token");

            /**
             * 토큰 만료 및 refresh는 나중에 처리
             */
            refresh_token = (String) map.get("refresh_token");
            expires_in = (int) map.get("expires_in");
            refresh_token_expires_in = (int) map.get("refresh_token_expires_in");

            log.info("token : {} ", access_token);
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

            throw new AuthorizationCodeNotFoundException(error_description);
        }
        return access_token;
    }

    private boolean validateToken(String access_token) throws IOException {
        HttpURLConnection connection = getConnection(KakaoApiConstants.URLs.VALIDATE_TOKEN_URL, "GET", false);
        connection.setRequestProperty("Authorization", "Bearer " + access_token);

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

            // exception 만들어서 처리!!!!!!!!!!!! NotValidTokenException
            throw new RuntimeException(msg);
        }
    }

    private KakaoMemberInfo getKakaoUserInfo(String access_token) throws IOException {

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
        connection.setRequestProperty("Authorization", "Bearer " + access_token);

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

            kakaoMemberInfo = new KakaoMemberInfo();
            kakaoMemberInfo.setKakaoMemberId(id);
            kakaoMemberInfo.setNickname(nickname);
            kakaoMemberInfo.setEmail(email);
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
    @AllArgsConstructor
    static class Result<Member> {
        private boolean isNewMember;
        private Member member;
    }

    @Getter
    @Setter
    class KakaoMemberInfo {
        private Long kakaoMemberId;
        private String nickname;
        private String email;
    }
}
