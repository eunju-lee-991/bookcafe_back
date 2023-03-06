package cafe_in.cafe_in.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberPostForm {
    private Long id;
    private String nickname;
    private String email;
    private String profileImageUrl;
}
