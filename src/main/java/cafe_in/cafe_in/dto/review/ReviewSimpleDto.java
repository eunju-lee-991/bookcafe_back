package cafe_in.cafe_in.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewSimpleDto {
    private Long reviewId;
    private String title;
    private Long isbn;
    private String bookTitle;
    private String updatedDate;
}
