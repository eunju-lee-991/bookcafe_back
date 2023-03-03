package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.domain.Review;
import cafe_in.cafe_in.dto.review.*;
import cafe_in.cafe_in.repository.review.ReviewSearch;
import cafe_in.cafe_in.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity CreateReview(@RequestBody ReviewForm reviewForm){ // JSON key는 대소문자도 구분
        Review review = new Review();
        review.setMemberId(reviewForm.getMemberId());
        review.setTitle(reviewForm.getTitle());
        review.setContents(reviewForm.getContents());
        review.setIsbn(reviewForm.getIsbn());
        review.setBookTitle(reviewForm.getBookTitle());
        review.setCreatedDate(LocalDateTime.now());
        review.setUpdatedDate(review.getCreatedDate());

        // Long으로 id 넘겨서 client에서 다시 상세페이지로 갈 수 있도록하기. response로 id랑 또 뭐를 전달할지는 나중에 고민
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
        //return reviewService.createReview(review);
    }

    @GetMapping("/reviews")
    public ReviewListResponse findReviews(@RequestBody(required = false) ReviewSearch reviewSearch){
        List<Review> reviews = null;
        int totalCount = 0;

        if(reviewSearch == null){
            log.info("ReviewSearch null");
            totalCount = reviewService.getTotalCount();
            reviews = reviewService.findReviews();
        }else {
            log.info("ReviewSearch not null");
            totalCount = reviewService.getTotalCount(reviewSearch);
            reviews = reviewService.findReviewsByCriteria(reviewSearch);
        }

        List<ReviewSimpleDto> reviewSimpleDtos = reviews.stream().map(review -> new ReviewSimpleDto(review.getReviewId(), review.getTitle(), review.getIsbn(), review.getBookTitle()
                , review.getUpdatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")))).collect(Collectors.toList());

        return new ReviewListResponse(totalCount, reviews.size(), reviewSimpleDtos);
    }

    @GetMapping("/reviews/{reviewId}")
    public ReviewResponse findOneReview(@PathVariable("reviewId") Long reviewId){
        Review review = reviewService.findOneReview(reviewId);
        String createdDate = review.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        String updatedDate = review.getUpdatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        ReviewDetailDto dto = new ReviewDetailDto(review.getReviewId(), review.getMemberId(), review.getTitle(), review.getContents(), review.getIsbn(), review.getBookTitle()
                , createdDate, updatedDate);

        return new ReviewResponse(dto);
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
}
