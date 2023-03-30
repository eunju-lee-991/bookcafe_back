package cafe_in.cafe_in.dto.member;

import cafe_in.cafe_in.domain.Jwt;
import cafe_in.cafe_in.dto.jwt.CreateTokenResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginMemberResponse {
    private boolean newMember;
    private MemberDto member;
    private CreateTokenResponse tokenInfo;
}
