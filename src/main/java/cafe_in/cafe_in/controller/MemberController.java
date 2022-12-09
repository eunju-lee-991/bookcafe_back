package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.domain.Member;
import cafe_in.cafe_in.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/members/{id}")
    public Member findOne(@PathVariable("id") Long id) {
        Member findMember = memberService.findOne(id).orElse(null);
        return findMember;
    }

    @GetMapping("/members")
    public List<Member> findMembers() {
        return memberService.findMembers();
    }

    @PostMapping("/members")
    public Long createMember(@RequestBody MemberKakaoForm request) { // *** json stringify로 보내고 RequestBody!!
        log.info("createMember " + request.getNickname());

        Long joinedId = memberService.join(request);

        return joinedId;
    }


}
