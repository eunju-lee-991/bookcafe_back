package cafe_in.cafe_in.service;

import cafe_in.cafe_in.controller.MemberKakaoForm;
import cafe_in.cafe_in.domain.Member;
import cafe_in.cafe_in.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private Long mylong;

    public Long join(MemberKakaoForm form){
        memberRepository.findOne(form.getId()).ifPresent(member -> {throw new IllegalStateException("jgjhgjhghb"); });

        /*memberRepository.findOne(form.getId()).ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다."); // list를 optional로 감싸면 return도 throw exception도 안 먹힘
        });*/
/*        Optional<List<Member>> one = memberRepository.findOne(form.getId());
        one
        .ifPresent(m -> { return ;} );*/

        return memberRepository.join(form);
    }

    public List<Member> findMembers(){
        return memberRepository.findMembers();
    }

    public Optional<Member> findOne(Long id) {
        return memberRepository.findOne(id);
    //    return memberRepository.findOne(id).orElse(null);
    }
}
