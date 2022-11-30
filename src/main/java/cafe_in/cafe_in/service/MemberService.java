package cafe_in.cafe_in.service;

import cafe_in.cafe_in.controller.MemberKakaoForm;
import cafe_in.cafe_in.domain.Member;
import cafe_in.cafe_in.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public int join(MemberKakaoForm form){
        return memberRepository.save(form);
    }

    public List<Member> findMember(Long id) {
        return memberRepository.findMemberById(id);
    }
}
