package cafe_in.cafe_in.repository.review;

import cafe_in.cafe_in.domain.Review;
import cafe_in.cafe_in.dto.review.ReviewDetailDto;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    public Long createReview(Review review);
    public List<Review> findReviews();
    public List<Review> findReviewsByCriteria(ReviewSearch reviewSearch);
    public Optional<ReviewDetailDto> findOneReview(Long reviewId);

    public int getReviewIdCount(Long reviewId);
    public int getTotalCount();
    public int getTotalCount(ReviewSearch reviewSearch);
    public int updateReview(Review review);
    public int deleteReview(Long reviewId);
}
