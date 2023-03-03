package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.domain.Member;
import cafe_in.cafe_in.dto.MemberForm;
import cafe_in.cafe_in.dto.MemberResultDto;
import cafe_in.cafe_in.exception.MemberNotFoundException;
import cafe_in.cafe_in.repository.member.MemberSearch;
import cafe_in.cafe_in.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/{id}")
    public Member findOne(@PathVariable("id") Long id) {
        return memberService.findOne(id);
    }

    @GetMapping("/members")
    public Result findMembers(@RequestBody(required = false) MemberSearch memberSearch) {
        List<Member> members
                = memberSearch == null ? memberService.findMembers() : memberService.findMembersByCriteria(memberSearch);

        if(members == null){
            System.out.println("null");
        }else {
            System.out.println("not nulll");
        }
        List<MemberResultDto> result = members.stream().map(m -> new MemberResultDto(m.getId(), m.getNickname(),m.getEmail())).collect(Collectors.toList());

        return new Result(result.size(), result);
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberForm memberForm) { // *** client에서 json stringify로 보내고 server에서 RequestBody로 받음
        Member member = new Member();
        member.setId(memberForm.getId());
        member.setNickname(memberForm.getNickname());
        member.setEmail(memberForm.getEmail());

        memberService.join(member);
        log.info("joined Id : {} ", member.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    @DeleteMapping("/members/{id}")
    public int deleteMember(@PathVariable Long id) {
        return memberService.deleteMember(id);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }
}
