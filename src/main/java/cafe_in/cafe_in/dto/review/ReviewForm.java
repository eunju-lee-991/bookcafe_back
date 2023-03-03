package cafe_in.cafe_in.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewForm {
    Long memberId;
    String title;
    String contents;
    Long isbn;
    String bookTitle;
}
