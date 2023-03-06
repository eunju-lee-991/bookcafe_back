package cafe_in.cafe_in.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDetailDto {
    Long reviewId;
    Long memberId;
    String nickname;
    String title;
    String contents;
    Long isbn;
    String bookTitle;
    String createdDate;
    String updatedDate;
}
