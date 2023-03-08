package cafe_in.cafe_in.service;

import cafe_in.cafe_in.domain.Review;
import cafe_in.cafe_in.dto.review.ReviewDetailDto;
import cafe_in.cafe_in.exception.ReviewNotFoundException;
import cafe_in.cafe_in.repository.member.MemberSearchOrder;
import cafe_in.cafe_in.repository.review.ReviewRepository;
import cafe_in.cafe_in.repository.review.ReviewSearch;
import cafe_in.cafe_in.repository.review.ReviewSearchOrder;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public Long createReview(Review review) {
        return reviewRepository.createReview(review);
    }

    public int getTotalCount(){

        return reviewRepository.getTotalCount();
    }

    public int getTotalCount(ReviewSearch reviewSearch){
        setReviewSearch(reviewSearch);

        return reviewRepository.getTotalCount(reviewSearch);
    }

    public List<Review> findReviews() {
        return reviewRepository.findReviews();
    }

    public List<Review> findReviewsByCriteria(ReviewSearch reviewSearch) {
        setReviewSearch(reviewSearch);

        return reviewRepository.findReviewsByCriteria(reviewSearch);
    }

    public ReviewDetailDto findOneReview(Long id) {
        checkExistence(id);

        return reviewRepository.findOneReview(id).orElseThrow(() -> new RuntimeException("리뷰 조회에 실패하였습니다."));
    }

    public Long updateReview(Review review) {
        checkExistence(review.getReviewId());

        if(reviewRepository.updateReview(review) != 0){
            return review.getReviewId();
        }else {
            throw new RuntimeException("리뷰 수정에 실패하였습니다.");
        }
    }

    public Long deleteReview(Long reviewId) {
        checkExistence(reviewId);

        if(reviewRepository.deleteReview(reviewId) != 0){
            return reviewId;
        }else {
            throw new RuntimeException("리뷰 삭제에 실패하였습니다.");
        }
    }

    private void checkExistence(Long reviewId) {
        int result = reviewRepository.getReviewIdCount(reviewId);
        if(result < 1){
            throw new ReviewNotFoundException("존재하지 않는 글번호입니다.");
        }
    }

    private void setReviewSearch(ReviewSearch reviewSearch){
        if (reviewSearch.getOrder() == null) { // 순서 설정이 없을 경우 기본으로 REVIEWID order로 세팅
            reviewSearch.setOrder(ReviewSearchOrder.REVIEWID);
        }

        if (reviewSearch.getSize() == 0){
            reviewSearch.setSize(10); // 설정 안했을 경우 기본값 10
        }
    }
}
