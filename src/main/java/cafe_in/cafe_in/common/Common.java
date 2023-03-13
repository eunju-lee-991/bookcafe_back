package cafe_in.cafe_in.common;

import cafe_in.cafe_in.controller.constant.SessionConstants;
import cafe_in.cafe_in.domain.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Common {

    public static Long getId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        return member.getId();
    }
}
