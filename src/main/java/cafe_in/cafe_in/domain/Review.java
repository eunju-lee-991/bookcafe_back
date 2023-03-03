package cafe_in.cafe_in.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.time.LocalDateTime;

@Getter
@Setter
public class Review {
    Long reviewId;
    Long memberId;
    String title;
    String contents;
    Long isbn;
    String bookTitle;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
}
