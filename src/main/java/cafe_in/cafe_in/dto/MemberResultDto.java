package cafe_in.cafe_in.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.ConstructorArgs;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MemberResultDto {
    private Long id;
    private String nickname;
    private String email;
}
