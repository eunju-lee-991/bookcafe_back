package cafe_in.cafe_in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.json.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@RestController
@Slf4j
public class KakaoLoginRestApiController {

    String restApiKey = "7bcfc7029ccb017c031af94a6e2dd46a";
    String redirectUrl = "http://localhost:8080/api/token";
    String requestUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + restApiKey + "&redirect_uri=" + redirectUrl;
    String tokenRequestUrl = "https://kauth.kakao.com/oauth/token";
    String memberInfoUrl = "https://kapi.kakao.com/v2/user/me";
    String token = "";
    String logoutUrl = "https://kapi.kakao.com/v1/user/logout";

    //@GetMapping("/api/login")
    public String kakaoLogin() throws IOException {
        URL url = null;
        HttpURLConnection connection = null;

        log.info("requestUrl : " + requestUrl);

        try {
            url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        log.info("response code : " + responseCode);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer stringBuffer = new StringBuffer();
        String inputLine;

        while ((inputLine = bufferedReader.readLine()) != null)  {
            stringBuffer.append(inputLine);
        }
        bufferedReader.close();

        String response = stringBuffer.toString();
        log.info("response : " + response);


        return "logged-in"; // 뭐 리턴하지?
    }

    @GetMapping("/api/token")
    public String getToken(@RequestParam String code) throws IOException {
        log.info("CODE : " + code);

        URL url = null;
        HttpURLConnection connection = null;

        url = new URL(tokenRequestUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);


        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        StringBuilder sb = new StringBuilder();

        sb.append("grant_type=authorization_code");
        sb.append("&client_id="+restApiKey);
        sb.append("&redirect_uri="+redirectUrl);
        sb.append("&code="+code);
        bw.write(sb.toString());
        bw.flush();

        int responseCode = connection.getResponseCode();
        log.info("response code : " + responseCode);

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        log.info("response body : " + result);

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, String> map2 = objectMapper.readValue(result, Map.class);

        JsonParser jsonParser = new JacksonJsonParser();

        Map<String, Object> map = jsonParser.parseMap(result);
        token = (String) map.get("access_token");
        int expires_in = (int) map.get("expires_in");

        log.info("token ; " + token);
        log.info("expires_id  : " + expires_in);

        return "";
    }

    @GetMapping("/api/member")
    public String member() throws IOException {

        URL url = null;
        HttpURLConnection connection = null;

        url = new URL(memberInfoUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        connection.setRequestProperty("Authorization", "Bearer "+token);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        StringBuilder sb = new StringBuilder();

       // String[] propertyKeys = {"kakao_account.profile", "kakao_account.email", "kakao_account.name"};

        String propertyKeys = "[\"kakao_account.email\", \"kakao_account.profile\"]"; //name

        sb.append("property_keys="+propertyKeys);
        bw.write(sb.toString());
        bw.flush();

        int responseCode = connection.getResponseCode();
        log.info("member 조회 response code : " + responseCode);


        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        log.info("member 조회  response body : " + result);

        return "result";
    }

    @GetMapping("/api/logout")
    public String kakaoLogout() throws IOException {
        URL url = null;
        HttpURLConnection connection = null;

        url = new URL(logoutUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Authorization", "Bearer "+token);


        int responseCode = connection.getResponseCode();
        log.info("response code : " + responseCode);


        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        log.info("LOGOUT response body : " + result);


        return "logged-out";
    }
}
