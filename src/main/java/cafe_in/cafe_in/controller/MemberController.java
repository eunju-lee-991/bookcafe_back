package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.domain.Member;
import cafe_in.cafe_in.dto.member.DeleteMemberResponse;
import cafe_in.cafe_in.dto.member.MemberForm;
import cafe_in.cafe_in.dto.member.MemberDto;
import cafe_in.cafe_in.dto.member.MemberListResponse;
import cafe_in.cafe_in.repository.member.MemberSearch;
import cafe_in.cafe_in.service.MemberService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberForm memberForm) { // *** client에서 json stringify로 보내고 server에서 RequestBody로 받음
        Member member = new Member();
        member.setId(memberForm.getId());
        member.setNickname(memberForm.getNickname());
        member.setEmail(memberForm.getEmail());

        memberService.join(member);
        log.info("joined Id : {} ", member.getId());

        MemberDto dto = new MemberDto(member.getId(), member.getNickname(), member.getEmail()
                , member.getJoinDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/members/{id}")
    public MemberDto findOne(@PathVariable("id") Long id) {
        Member member = memberService.findOne(id);
        MemberDto dto = new MemberDto(member.getId(), member.getNickname(), member.getEmail()
                , member.getJoinDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        return dto;
    }

    @GetMapping("/members")
    public MemberListResponse findMembers(@RequestBody(required = false) MemberSearch memberSearch) {
        List<Member> members
                = memberSearch == null ? memberService.findMembers() : memberService.findMembersByCriteria(memberSearch);
//
//        if(members == null){
//            System.out.println("null");
//        }else {
//            System.out.println("not nulll");
//        }
        List<MemberDto> result = members.stream().map(m -> new MemberDto(m.getId(), m.getNickname(),m.getEmail()
                , m.getJoinDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")))).collect(Collectors.toList());

        return new MemberListResponse(result.size(), result);
    }

    @DeleteMapping("/members/{id}")
    public DeleteMemberResponse deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return new DeleteMemberResponse(id);
    }

}
