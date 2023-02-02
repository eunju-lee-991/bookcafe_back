package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/test")
    public KakaoTokenRequset test(@RequestBody KakaoTokenRequset req) {

        return req;
    }

    @GetMapping("/login/token")
    public KakaoTokenResponse getToken(@RequestBody KakaoTokenRequset req) throws IOException {
        HttpURLConnection connection = getConnection(new URL(KakaoApiConstants.TOKEN_REQUEST_URI), "POST", true);
        setTokenRequestOutPut(connection, req);

        int responseCode = connection.getResponseCode();

        String input = readInPut(connection);
        //KakaoTokenResponse response = readJsonData(input);
        Map<String, Object> kakaoTokenMap = readJsonData(input);
        String token = (String) kakaoTokenMap.get("access_token");


        // 토큰으로 회원정보 조회
//Member kakaoUser = getKakaoUser
        // 조회한 회원정보로 회원가입 여부 확인
//memberService.isExistingMember(kakaoUser.getId())
        // 회원 아니면 회원가입
//memberService.join(memberForm)
        // 회원이면 로그인
//memberService.findOne() optional
        // 세션에 토큰이랑 회원정보 저장 후 쿠키

        return null;
    }

    private HttpURLConnection getConnection(URL url, String requestMethod, boolean doOutPut) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.setDoOutput(doOutPut);

        return connection;
    }

    private void setTokenRequestOutPut(HttpURLConnection connection, KakaoTokenRequset req) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");
        sb.append("&client_id="+req.getClient_id());
        sb.append("&redirect_uri="+req.getRedirect_uri());
        sb.append("&code="+req.getCode());
        bw.write(sb.toString());
        bw.flush();
    }

    private String readInPut(HttpURLConnection connection) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        return result;
    }

    private Map<String, Object> readJsonData (String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> res =  objectMapper.readValue(data, new TypeReference<Map<String,Object>>(){});

        return res;
    }

    @Getter
    @Setter
    static class KakaoTokenRequset {
        private String client_id;
        private String redirect_uri;
        private String code;
    }

    @Getter
    @Setter
    static class KakaoTokenResponse {
        private String token_type;
        private String access_token;
        private Integer expires_in;
        private String refresh_token;
        private String scope;
        private Integer refresh_token_expires_in;
    }
}
