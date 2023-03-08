package cafe_in.cafe_in.dto.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PostMemberForm {
    @NotNull
    private Long id;
    @Size(min = 1, max = 50)
    private String nickname;
    @NotBlank
    private String email;
    private String profileImageUrl;
}
