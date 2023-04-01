package cafe_in.cafe_in.common;

import cafe_in.cafe_in.controller.constant.SessionConstants;
import cafe_in.cafe_in.domain.Member;
import com.nimbusds.jose.Header;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Optional;

public class Common {

    public static Long getId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        return member.getId();
    }

    public static Cookie setCookie(String cookieName, String value, boolean isHttpOnly, boolean isSecure, int maxAge, String path) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setHttpOnly(isHttpOnly);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setSecure(isSecure); // HTTPS 프로토콜에서만 쿠키 전송 가능
        return cookie;
    }

    public static String getCookieValue(Cookie[] cookies, String cookieName) {
        String result = null;
        Optional<Cookie> cookie = Arrays.stream(cookies).filter(c -> c.getName().equals(cookieName)).findFirst();
        if (cookie.isPresent()) {
            result = cookie.get().getValue();
        }

        return result;
    }

}
