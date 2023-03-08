package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.domain.Review;
import cafe_in.cafe_in.dto.review.*;
import cafe_in.cafe_in.exception.BindingFieldFailException;
import cafe_in.cafe_in.repository.review.ReviewSearch;
import cafe_in.cafe_in.repository.review.ReviewSearchOrder;
import cafe_in.cafe_in.service.MemberService;
import cafe_in.cafe_in.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;

    @PostMapping("/reviews")
    public ResponseEntity CreateReview(@Valid @RequestBody PostReviewForm postReviewForm, BindingResult bindingResult) { // JSON key는 대소문자도 구분
        if(bindingResult.hasFieldErrors()){
            throw new BindingFieldFailException(bindingResult.getFieldErrors().stream().findFirst().get());
        }

        Review review = new Review();

        review.setMemberId(postReviewForm.getMemberId());
        review.setTitle(postReviewForm.getTitle());
        review.setContents(postReviewForm.getContents());
        review.setIsbn(postReviewForm.getIsbn());
        review.setBookTitle(postReviewForm.getBookTitle());
        review.setCreatedDate(LocalDateTime.now());
        review.setUpdatedDate(review.getCreatedDate());
        Long reviewId = reviewService.createReview(review);

        return ResponseEntity.status(HttpStatus.CREATED).body(new PostReviewResponse(reviewId));
    }

    @GetMapping("/reviews")
    public ReviewListResponse findReviews(@RequestParam(required = false) Map<String, String> params) {
        List<Review> reviews = null;
        int totalCount = 0;
        boolean isEnd = false;

        if (params.size() < 1) { // 아무 조건 없이 회원 전체 가져오기
            log.info("ReviewSearch null");
            totalCount = reviewService.getTotalCount();
            reviews = reviewService.findReviews();
            isEnd = true; // 회원 전체이기 때문에 isEnd = true
        } else {
            ReviewSearch reviewSearch = convertParamMapToReviewSearch(params);
            log.info("ReviewSearch not null");
            totalCount = reviewService.getTotalCount(reviewSearch);
            reviews = reviewService.findReviewsByCriteria(reviewSearch);
            isEnd = (reviewSearch.getPage() + 1) * reviewSearch.getSize() >= totalCount ? true : false;
        }

        List<ReviewSimpleDto> reviewSimpleDtos = reviews.stream().map(review -> new ReviewSimpleDto(review.getReviewId(), review.getTitle(), review.getIsbn(), review.getBookTitle()
                , review.getUpdatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")))).collect(Collectors.toList());

        return new ReviewListResponse(totalCount, isEnd, reviewSimpleDtos); //전체 게시물 count, offset&limit 적용 후 count , dto
    }

    @GetMapping("/reviews/{reviewId}")
    public ReviewResponse findOneReview(@PathVariable("reviewId") Long reviewId) {
        ReviewDetailDto reviewDetailDto = reviewService.findOneReview(reviewId);

        return new ReviewResponse(reviewDetailDto);
    }

    @PatchMapping("/reviews/{reviewId}")
    public UpdateReviewResponse updateReview(@PathVariable("reviewId") Long reviewId, @Valid @RequestBody UpdateReviewForm updateReviewForm, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()){
            throw new BindingFieldFailException(bindingResult.getFieldErrors().stream().findFirst().get());
        }

        Review review = new Review();
        review.setReviewId(reviewId);
        review.setTitle(updateReviewForm.getTitle());
        review.setContents(updateReviewForm.getContents());
        review.setUpdatedDate(LocalDateTime.now());

        return new UpdateReviewResponse(reviewService.updateReview(review));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public DeleteReviewReponse delete(@PathVariable("reviewId") Long reviewId) {
        return new DeleteReviewReponse(reviewService.deleteReview(reviewId));
    }

    private ReviewSearch convertParamMapToReviewSearch(Map<String, String> params) {
        ReviewSearch reviewSearch = new ReviewSearch();

        if (params.containsKey("memberId")) {
            reviewSearch.setMemberId(params.get("memberId"));
        }
        if (params.containsKey("title")) {
            reviewSearch.setTitle(params.get("title"));
        }
        if (params.containsKey("contents")) {
            reviewSearch.setContents(params.get("contents"));
        }
        if (params.containsKey("bookTitle")) {
            reviewSearch.setBookTitle(params.get("bookTitle"));
        }
        if (params.containsKey("order")) {
            reviewSearch.setOrder(ReviewSearchOrder.valueOf(params.get("order")));
        }
        if (params.containsKey("page")) {
            int page = Integer.valueOf(params.get("page"));
            if(page < 1 ){
                throw new IllegalArgumentException("Page는 1 이상의 숫자여야합니다.");
            }
            reviewSearch.setPage(page - 1); // offset으로 사용할 거여서 -1
        }
        if (params.containsKey("size")) {
            int size = Integer.valueOf(params.get("size"));
            if(size < 1){
                throw new IllegalArgumentException("Size는 1 이상의 숫자여야합니다.");
            }
            reviewSearch.setSize(size);
        }

        return reviewSearch;
    }
}
