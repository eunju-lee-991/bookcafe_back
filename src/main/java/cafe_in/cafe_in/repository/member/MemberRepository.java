package cafe_in.cafe_in.repository.member;

import cafe_in.cafe_in.domain.Member;
import java.util.List;
import java.util.Optional;


public interface MemberRepository {
    public void join(Member member);
    public List<Member> findMembers();
    public List<Member> findMembersByCriteria(MemberSearch memberSearch);
    public Optional<Member> findOne(Long id);
    public int deleteOne(Long id);
    int updateMember(Member member);
}
