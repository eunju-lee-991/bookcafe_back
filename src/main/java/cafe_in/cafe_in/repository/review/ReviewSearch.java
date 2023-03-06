package cafe_in.cafe_in.repository.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewSearch {
    String memberId;
    String title;
    String contents;
    String bookTitle;
    //String nickname; // nickname으로 memberId 찾아서 찾아오기 나중에
    ReviewSearchOrder order;
    int page;
    int size;
}
