package cafe_in.cafe_in.repository.review;

import cafe_in.cafe_in.dto.review.ReviewDetailDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

@Component
public class ReviewDetailDtoRowMapper implements RowMapper<ReviewDetailDto> {
    @Override
    public ReviewDetailDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReviewDetailDto reviewDetailDto = new ReviewDetailDto();

        reviewDetailDto.setReviewId(rs.getLong("reviewid")); //ResultSet get할 때는 대소문자 구분X
        reviewDetailDto.setMemberId(rs.getLong("memberid"));
        reviewDetailDto.setNickname(rs.getString("nickname"));
        reviewDetailDto.setTitle(rs.getString("title"));
        reviewDetailDto.setContents(rs.getString("contents"));
        reviewDetailDto.setIsbn(rs.getLong("isbn"));
        reviewDetailDto.setBookTitle(rs.getString("booktitle"));
        reviewDetailDto.setLikeCount(rs.getLong("likeCount"));

        String createdDate = rs.getTimestamp("createddate").toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        String updatedDate = rs.getTimestamp("updateddate").toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        reviewDetailDto.setCreatedDate(createdDate);
        reviewDetailDto.setUpdatedDate(updatedDate);

        return reviewDetailDto;
    }
}
