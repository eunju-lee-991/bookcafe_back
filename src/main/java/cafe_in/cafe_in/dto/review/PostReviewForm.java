package cafe_in.cafe_in.dto.review;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PostReviewForm {
    @Size(min = 1, max = 200)
    String title;
    @Size(min = 1, max = 3000)
    String contents;
    @NotNull
    Long isbn;
    @Size(min = 1, max = 200)
    String bookTitle;
}
