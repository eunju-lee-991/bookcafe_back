package cafe_in.cafe_in.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Member {
    private Long id;
    private String nickname;
    private String email;
    private LocalDateTime joinDate;
}
