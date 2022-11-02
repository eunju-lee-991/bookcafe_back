package cafe_in.cafe_in.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class MemberController {

    @GetMapping("/members/{id}")
    @ResponseBody
    public String checkMember(@PathVariable("id") Long id) {
        log.info(id + " 회원이 존재하는지 체크");
        int result = 0;
        return result > 0 ? "exist" : "join";
    }

    @PostMapping("/members")
    @ResponseBody
    public String createMember(@RequestBody MemberKakaoForm request) { // *** json stringify로 보내고 RequestBody!!
        log.info(request.getEmail());
        log.info("request " + request.getNickname());

        return "success 제이슨 데이터";
    }


}
