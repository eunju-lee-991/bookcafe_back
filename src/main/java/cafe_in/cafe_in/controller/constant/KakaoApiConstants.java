package cafe_in.cafe_in.controller.constant;

public class KakaoApiConstants {

    public static class URLs {
        public static final String GET_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
        public static final String REDIRECT_URI = "http://localhost:5000/login/token";
        public static final String VALIDATE_TOKEN_URL = "https://kapi.kakao.com/v1/user/access_token_info";
        public static final String GET_USER_INFO = "https://kapi.kakao.com/v2/user/me";
        public static final String LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";
    }

}
