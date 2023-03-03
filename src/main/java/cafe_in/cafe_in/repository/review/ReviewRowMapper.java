package cafe_in.cafe_in.repository.review;

import cafe_in.cafe_in.domain.Review;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class ReviewRowMapper implements RowMapper<Review> {

    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getLong("reviewid")); //ResultSet get할 때는 대소문자 구분X
        review.setMemberId(rs.getLong("memberid"));
        review.setTitle(rs.getString("title"));
        review.setContents(rs.getString("contents"));
        review.setIsbn(rs.getLong("isbn"));
        review.setBookTitle(rs.getString("booktitle"));
        review.setCreatedDate(rs.getTimestamp("createddate").toLocalDateTime());
        review.setUpdatedDate(rs.getTimestamp("updateddate").toLocalDateTime());

        return review;
    }
}
