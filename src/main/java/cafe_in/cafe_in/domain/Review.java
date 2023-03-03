package cafe_in.cafe_in.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.time.LocalDateTime;

@Getter
@Setter
public class Review {
    private Long reviewId;
    private Long memberId;
    private String title;
    private String contents;
    private Long isbn;
    private String bookTitle;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
