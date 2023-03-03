package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.domain.Review;
import cafe_in.cafe_in.dto.ReviewForm;
import cafe_in.cafe_in.repository.review.ReviewRepositoryImpl;
import cafe_in.cafe_in.repository.review.ReviewSearch;
import cafe_in.cafe_in.repository.review.ReviewSearchOrder;
import cafe_in.cafe_in.repository.review.ReviewSql;
import cafe_in.cafe_in.service.MemberService;
import cafe_in.cafe_in.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public Long CreateReview(@RequestBody ReviewForm reviewForm){ // JSON key는 대소문자도 구분
        Review review = new Review();
        review.setMemberId(reviewForm.getMemberId());
        review.setTitle(reviewForm.getTitle());
        review.setContents(reviewForm.getContents());
        review.setIsbn(reviewForm.getIsbn());
        review.setBookTitle(reviewForm.getBookTitle());
        review.setCreatedDate(LocalDateTime.now());
        review.setUpdatedDate(review.getCreatedDate());

        // Long으로 id 넘겨서 client에서 다시 상세페이지로 갈 수 있도록하기. response로 id랑 또 뭐를 전달할지는 나중에 고민

        return reviewService.createReview(review);
    }

    @GetMapping("/reviews")
    public FindReviewResponse findReviews(@RequestBody(required = false) ReviewSearch reviewSearch){
        List<Review> reviews = null;
        int totalCount = 0;

        if(reviewSearch == null){
            log.info("ReviewSearch null");
            totalCount = reviewService.getTotalCount();
            reviews = reviewService.findReviews();
        }else {
            log.info("ReviewSearch not null");
            if (reviewSearch.getOrder() == null) { // 순서 설정이 없을 경우 기본으로 REVIEWID order로 세팅
                reviewSearch.setOrder(ReviewSearchOrder.REVIEWID);
            }

            if (reviewSearch.getLimit() == 0){
                reviewSearch.setLimit(10); // limit 설정 안했을 경우 기본값 10
            }

            totalCount = reviewService.getTotalCount(reviewSearch);
            reviews = reviewService.findReviewsByCriteria(reviewSearch);
        }


        return new FindReviewResponse(totalCount, reviews.size(), reviews);
    }

    @GetMapping("/reviews/{reviewId}")
    public Review findOneReview(@PathVariable("reviewId") Long reviewId){
        return reviewService.findOneReview(reviewId);
    }

    @PatchMapping("/reviews/{reviewId}")
    public Long updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewForm reviewForm){
        Review review = new Review();
        review.setReviewId(reviewId);
        review.setTitle(reviewForm.getTitle());
        review.setContents(reviewForm.getContents());
        review.setUpdatedDate(LocalDateTime.now());

        // Long으로 id 넘겨서 client에서 다시 상세페이지로 갈 수 있도록하기. id랑 status?또 뭐를 전달할지는 나중에 고민

        return reviewService.updateReview(review);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public Long delete(@PathVariable("reviewId") Long reviewId){
        return reviewService.deleteReview(reviewId);
    }

    class CreateReviewResponse {
        Long id;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    class FindReviewResponse {
        int total_count;
        int count;
        List<Review> review;
    }

    class UpdateReviewResponse {

    }
}
