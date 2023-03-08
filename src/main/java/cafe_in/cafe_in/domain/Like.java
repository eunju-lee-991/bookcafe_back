package cafe_in.cafe_in.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Like {
    private Long likeId;
    private Long reviewId;
    private Long memberId;
    private LocalDateTime clickDate;
}
