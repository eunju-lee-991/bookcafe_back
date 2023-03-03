package cafe_in.cafe_in.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Member {
    Long id;
    String nickname;
    String email;
    LocalDateTime joinDate;
}
