package cafe_in.cafe_in.service;

import cafe_in.cafe_in.domain.Review;
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

        return reviewRepository.getTotalCount(reviewSearch);
    }

    public List<Review> findReviews() {
        return reviewRepository.findReviews();
    }

    public List<Review> findReviewsByCriteria(ReviewSearch reviewSearch) {


        return reviewRepository.findReviewsByCriteria(reviewSearch);
    }

    public Review findOneReview(Long id) {

        // Exception 만들어서 처리하기
        return reviewRepository.findOneReview(id).orElseThrow(() -> new RuntimeException("존재하지 않는 글번호입니다."));
    }

    public Long updateReview(Review review) {
        if(reviewRepository.updateReview(review) != 0){
            return review.getReviewId();
        }else {
            throw new RuntimeException("존재하지 않는 글번호입니다.");// Exception 만들어서 처리하기
        }
    }

    public Long deleteReview(Long reviewId) {
        if(reviewRepository.deleteReview(reviewId) != 0){
            return reviewId;
        }else {
            throw new RuntimeException("존재하지 않는 글번호입니다.");// Exception 만들어서 처리하기
        }
    }
}
