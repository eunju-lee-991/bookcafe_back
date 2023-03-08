package cafe_in.cafe_in.repository.like;

import cafe_in.cafe_in.domain.Like;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikeRowMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
        Like like = new Like();
        like.setLikeId(rs.getLong("likeId"));
        like.setReviewId(rs.getLong("reviewId"));
        like.setMemberId(rs.getLong("memberId"));
        like.setClickDate(rs.getTimestamp("clickDate").toLocalDateTime());

        return like;
    }
}
