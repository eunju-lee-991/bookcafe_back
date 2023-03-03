package cafe_in.cafe_in.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReviewListResponse {
    private int total_count;
    private int count;
    private List<ReviewSimpleDto> reviews;
}
