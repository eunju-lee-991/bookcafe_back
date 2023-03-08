package cafe_in.cafe_in.repository.like;

import cafe_in.cafe_in.domain.Like;
import cafe_in.cafe_in.repository.review.ReviewSql;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final LikeRowMapper likeRowMapper;

    @Override
    public Long createLike(Like like) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(LikeSql.INSERT_LIKE, new BeanPropertySqlParameterSource(like), keyHolder);

        return (Long) keyHolder.getKeys().get("LIKEID");
    }

    @Override
    public Optional<Like> findLike(Long reviewId, Long memberId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("reviewId", reviewId);
        paramMap.addValue("memberId", memberId);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(namedParameterJdbcTemplate.query(LikeSql.SELECT_LIKE, paramMap, likeRowMapper)));
    }

    @Override
    public int deleteLike(Long likeId) {
        SqlParameterSource param = new MapSqlParameterSource("likeId", likeId);

        return namedParameterJdbcTemplate.update(LikeSql.DELETE_LIKE, param);
    }

    @Override
    public int getCountForLikeId(Long likeId) {
        SqlParameterSource param = new MapSqlParameterSource("likeId", likeId);
        return namedParameterJdbcTemplate.queryForObject(LikeSql.SELECT_COUNT_LIKEID, param, Integer.class );
    }
}
