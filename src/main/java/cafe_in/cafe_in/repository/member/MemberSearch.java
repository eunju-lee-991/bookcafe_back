package cafe_in.cafe_in.repository.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearch {
    String nickname;
    String email;
    MemberSearchOrder order;
}
