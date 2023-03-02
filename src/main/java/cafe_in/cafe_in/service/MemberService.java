package cafe_in.cafe_in.service;

import cafe_in.cafe_in.domain.Member;
import cafe_in.cafe_in.exception.DuplicateUserException;
import cafe_in.cafe_in.exception.MemberNotFoundException;
import cafe_in.cafe_in.repository.member.MemberRepository;
import cafe_in.cafe_in.repository.member.MemberRepositoryImpl;
import cafe_in.cafe_in.repository.member.MemberSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public boolean isExistingMember(Long id){
        return memberRepository.findOne(id).isPresent();
    }

    public Long join(Member member){
        if(isExistingMember(member.getId())){
            throw new DuplicateUserException(String.format("이미 존재하는 회원입니다. ID : %s ", member.getId()));
        }

        member.setJoinDate(LocalDateTime.now());
        memberRepository.join(member);

        return member.getId();
    }

    public Member findOne(Long id) {
        return memberRepository.findOne(id).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
    }

    public List<Member> findMembers(){
        return memberRepository.findMembers();
    }

    public List<Member> findMembersByCriteria(MemberSearch memberSearch){
        return memberRepository.findMembersByCriteria(memberSearch);
    }

    public int deleteMember(Long id) {
        //delete도 notfound 예외
        return memberRepository.deleteOne(id);
    }
}
