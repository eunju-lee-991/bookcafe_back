package cafe_in.cafe_in.repository.review;

import cafe_in.cafe_in.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    public void createReview(Review review);
    public List<Review> findReviews();
    public List<Review> findReviewsByCriteria(ReviewSearch reviewSearch);
    public Optional<Review> findOneReview(Long reviewId);
    public Review updateReview(Review review);
    public int deleteReview(Long reviewId);
}
