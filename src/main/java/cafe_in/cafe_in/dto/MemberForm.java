package cafe_in.cafe_in.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {
    Long id;
    String name;
    String nickname;
    String email;
}
