package cafe_in.cafe_in.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {
    Long id;
    String name;
    String nickname;
    String email;
    LocalDateTime joinDate;
}
