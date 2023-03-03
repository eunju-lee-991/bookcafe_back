package cafe_in.cafe_in.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String nickname;
    private String email;
    private String joinDate;
}
