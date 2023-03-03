package cafe_in.cafe_in.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginMemberResponse {
    private boolean newMember;
    private MemberDto member;
}
