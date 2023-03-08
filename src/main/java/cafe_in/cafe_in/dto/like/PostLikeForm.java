package cafe_in.cafe_in.dto.like;

import lombok.Data;

import javax.validation.constraints.NotNull;

//@Getter
//@Setter
@Data
public class PostLikeForm {
    @NotNull
    private Long reviewId;
    @NotNull
    private Long memberId;
}
