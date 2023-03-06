package cafe_in.cafe_in.service;

import cafe_in.cafe_in.domain.Member;
import cafe_in.cafe_in.exception.DuplicateUserException;
import cafe_in.cafe_in.exception.MemberNotFoundException;
import cafe_in.cafe_in.repository.member.MemberRepository;
import cafe_in.cafe_in.repository.member.MemberRepositoryImpl;
import cafe_in.cafe_in.repository.member.MemberSearch;
import cafe_in.cafe_in.repository.member.MemberSearchOrder;
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
        if (memberSearch.getOrder() == null) { // 순서 설정이 없을 경우 기본으로 ID 순서로 세팅
            memberSearch.setOrder(MemberSearchOrder.ID);
        }

        return memberRepository.findMembersByCriteria(memberSearch);
    }

    public Long updateMember(Member member) {
        if(!isExistingMember(member.getId())){
            throw new MemberNotFoundException("존재하지 않는 회원입니다");
        }

        if(memberRepository.updateMember(member) != 0){
            return member.getId();
        }else {
            throw new RuntimeException("회원정보 수정에 실패하였습니다.");
        }
    }

    public Long deleteMember(Long id) {
        if(!isExistingMember(id)){
            throw new MemberNotFoundException("존재하지 않는 회원입니다");
        }
        if(memberRepository.deleteOne(id) != 0){
            return id;
        }else {
            throw new RuntimeException("회원정보 삭제에 실패하였습니다.");
        }
    }
}
