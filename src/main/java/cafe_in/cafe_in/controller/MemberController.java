package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.domain.Member;
import cafe_in.cafe_in.dto.member.*;
import cafe_in.cafe_in.exception.BindingFieldFailException;
import cafe_in.cafe_in.repository.member.MemberSearch;
import cafe_in.cafe_in.service.MemberService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("")
    public ResponseEntity createMember(@Valid @RequestBody PostMemberForm postMemberForm, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()){
            throw new BindingFieldFailException(bindingResult.getFieldErrors().stream().findFirst().get());
        }

        Member member = new Member();
        member.setId(postMemberForm.getId());
        member.setNickname(postMemberForm.getNickname());
        member.setEmail(postMemberForm.getEmail());
        member.setProfileImageUrl(postMemberForm.getProfileImageUrl());

        memberService.join(member);

        MemberDto dto = new MemberDto(member.getId(), member.getNickname(), member.getEmail(), member.getProfileImageUrl()
                , member.getJoinDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public MemberDto findOne(@PathVariable("id") Long id) {
        Member member = memberService.findOne(id);
        MemberDto dto = new MemberDto(member.getId(), member.getNickname(), member.getEmail(), member.getProfileImageUrl()
                , member.getJoinDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        return dto;
    }

    @GetMapping("")
    public MemberListResponse findMembers(@RequestBody(required = false) MemberSearch memberSearch) { // parameter Map으로 수정!!!!
        List<Member> members
                = memberSearch == null ? memberService.findMembers() : memberService.findMembersByCriteria(memberSearch);

        List<MemberDto> result = members.stream().map(m -> new MemberDto(m.getId(), m.getNickname(),m.getEmail(), m.getProfileImageUrl()
                , m.getJoinDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")))).collect(Collectors.toList());

        return new MemberListResponse(result.size(), result);
    }

    @PatchMapping("/{id}")
    public UpdateMemberResponse updateMember(@PathVariable Long id, @Valid @RequestBody UpdateMemberForm updateMemberForm, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()){
            throw new BindingFieldFailException(bindingResult.getFieldErrors().stream().findFirst().get());
        }

        Member member = new Member();
        member.setId(id);
        member.setNickname(updateMemberForm.getNickname());
        member.setEmail(updateMemberForm.getEmail());

        Long updatedId = memberService.updateMember(member);

        return new UpdateMemberResponse(updatedId);
    }

    @DeleteMapping("/{id}")
    public DeleteMemberResponse deleteMember(@PathVariable Long id) {
        Long deletedId = memberService.deleteMember(id);
        return new DeleteMemberResponse(deletedId);
    }
}
