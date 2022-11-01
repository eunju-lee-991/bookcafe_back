package cafe_in.cafe_in.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class MemberController {

    @GetMapping("/checkMember")
    @ResponseBody
    public String checkMember(@RequestParam String id) {
        log.info("이제 여기서 회원가입 안되어 있으면 회원가입하면 됨 " + id);
        return "checkMember";
    }
}
