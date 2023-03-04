package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.domain.Member;
import cafe_in.cafe_in.domain.Review;
import cafe_in.cafe_in.dto.review.*;
import cafe_in.cafe_in.repository.review.ReviewSearch;
import cafe_in.cafe_in.service.MemberService;
import cafe_in.cafe_in.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
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
    private final MemberService memberService;

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
        Long reviewId = reviewService.createReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PostReviewResponse(reviewId));
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

        // namedParameterJdbcTemplate.query() 에서는 rowMapper를 2개 이상 전달할 수 없어서 join 쿼리 사용하지 않고 따로 서비스 호출해서 member 정보 불러왔음
        // dto 생성해서 처리하는 게 나을지 고민
        Member member = memberService.findOne(review.getMemberId());
        ReviewDetailDto dto = new ReviewDetailDto(review.getReviewId(), review.getMemberId(), member.getNickname(), review.getTitle()
                , review.getContents(), review.getIsbn(), review.getBookTitle() , createdDate, updatedDate);

        return new ReviewResponse(dto);
    }

    @PatchMapping("/reviews/{reviewId}")
    public UpdateReviewResponse updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewForm reviewForm){
        Review review = new Review();
        review.setReviewId(reviewId);
        review.setTitle(reviewForm.getTitle());
        review.setContents(reviewForm.getContents());
        review.setUpdatedDate(LocalDateTime.now());

        // Long으로 id 넘겨서 client에서 다시 상세페이지로 갈 수 있도록하기. id랑 status?또 뭐를 전달할지는 나중에 고민

        return new UpdateReviewResponse(reviewService.updateReview(review));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public DeleteReviewReponse delete(@PathVariable("reviewId") Long reviewId){
        return new DeleteReviewReponse(reviewService.deleteReview(reviewId));
    }
}
