package cafe_in.cafe_in.dto.review;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateReviewForm {

    @Size(min = 1, max = 200)
    String title;

    @Size(min = 1, max = 300)
    String contents;
}
