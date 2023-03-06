package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.domain.Member;
import cafe_in.cafe_in.dto.member.*;
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
    public ResponseEntity createMember(@RequestBody MemberPostForm memberPostForm) { // *** client에서 json stringify로 보내고 server에서 RequestBody로 받음
        Member member = new Member();
        member.setId(memberPostForm.getId());
        member.setNickname(memberPostForm.getNickname());
        member.setEmail(memberPostForm.getEmail());
        member.setProfileImageUrl(memberPostForm.getProfileImageUrl());

        memberService.join(member);
        log.info("joined Id : {} ", member.getId());

        MemberDto dto = new MemberDto(member.getId(), member.getNickname(), member.getEmail(), member.getProfileImageUrl()
                , member.getJoinDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/members/{id}")
    public MemberDto findOne(@PathVariable("id") Long id) {
        Member member = memberService.findOne(id);
        MemberDto dto = new MemberDto(member.getId(), member.getNickname(), member.getEmail(), member.getProfileImageUrl()
                , member.getJoinDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        return dto;
    }

    @GetMapping("/members")
    public MemberListResponse findMembers(@RequestBody(required = false) MemberSearch memberSearch) {
        List<Member> members
                = memberSearch == null ? memberService.findMembers() : memberService.findMembersByCriteria(memberSearch);

        List<MemberDto> result = members.stream().map(m -> new MemberDto(m.getId(), m.getNickname(),m.getEmail(), m.getProfileImageUrl()
                , m.getJoinDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")))).collect(Collectors.toList());

        return new MemberListResponse(result.size(), result);
    }

    @PatchMapping("/members/{id}")
    public UpdateMemberResponse updateMember(@PathVariable Long id, @RequestBody MemberUpdateForm memberUpdateForm) {
        Member member = new Member();
        member.setId(id);
        member.setNickname(memberUpdateForm.getNickname());
        member.setEmail(memberUpdateForm.getEmail());

        Long updatedId = memberService.updateMember(member);

        return new UpdateMemberResponse(updatedId);
    }
    @DeleteMapping("/members/{id}")
    public DeleteMemberResponse deleteMember(@PathVariable Long id) {
        Long deletedId = memberService.deleteMember(id);
        return new DeleteMemberResponse(deletedId);
    }
}
