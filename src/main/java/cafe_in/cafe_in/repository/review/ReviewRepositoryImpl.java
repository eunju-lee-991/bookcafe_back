package cafe_in.cafe_in.repository.review;

import cafe_in.cafe_in.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ReviewRowMapper reviewRowMapper; // SELECT해서 Mapping할 때 select한 컬럼과 mapper가 매핑하는 컬럼 일치해야함

    @Override
    public void createReview(Review review) {

    }

    @Override
    public List<Review> findReviews() {
        List<Review> query = namedParameterJdbcTemplate.query(ReviewSql.SELECT_REVIEW, reviewRowMapper);
        return query;
    }

    @Override
    public List<Review> findReviewsByCriteria(ReviewSearch reviewSearch) {
        return null;
    }

    @Override
    public Optional<Review> findOneReview(Long reviewId) {
        return Optional.empty();
    }

    @Override
    public Review updateReview(Review review) {
        return null;
    }

    @Override
    public int deleteReview(Long reviewId) {
        return 0;
    }
}
