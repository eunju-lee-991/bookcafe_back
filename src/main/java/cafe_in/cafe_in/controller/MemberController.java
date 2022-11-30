package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.domain.Member;
import cafe_in.cafe_in.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/{id}")
    @ResponseBody
    public String checkMember(@PathVariable("id") Long id) {
        log.info(id + " dho ektl Rowlsmsep?");

        List<Member> findMember = memberService.findMember(id);

        return findMember != null && findMember.size() > 0 ? "exist" : "join";
        // 데이터랑 COUNT 같이 리턴하는 걸로 바꾸기??
    }

    @PostMapping("/members")
    @ResponseBody
    public int createMember(@RequestBody MemberKakaoForm request) { // *** json stringify로 보내고 RequestBody!!
        log.info(request.getEmail());
        log.info("createMember requestform " + request.getNickname());

        int result = memberService.join(request);

        return result;
    }


}
