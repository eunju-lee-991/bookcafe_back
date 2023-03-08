package cafe_in.cafe_in.dto.like;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponse {
    private Long likeId;
    private String clickDate;
    private boolean isClicked;
}
