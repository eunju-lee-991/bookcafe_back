package cafe_in.cafe_in.repository.review;

import cafe_in.cafe_in.domain.Review;
import cafe_in.cafe_in.repository.member.MemberSql;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ReviewRowMapper reviewRowMapper; // SELECT해서 Mapping할 때 select한 컬럼과 mapper가 매핑하는 컬럼 일치해야함


    @Override
    public Long createReview(Review review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(ReviewSql.INSERT_REVIEW, new BeanPropertySqlParameterSource(review), keyHolder);
        return (Long) keyHolder.getKeys().get("REVIEWID");
    }

    @Override
    public Optional<Review> findOneReview(Long reviewId) {
        String findOneReviewSql = ReviewSql.SELECT_REVIEW + ReviewSql.WHERE_REVIEWID;
        SqlParameterSource param = new MapSqlParameterSource("reviewId", reviewId);

        return Optional.ofNullable(DataAccessUtils.uniqueResult(namedParameterJdbcTemplate.query(findOneReviewSql, param, reviewRowMapper)));
    }

    public int getTotalCount(){
        return namedParameterJdbcTemplate.queryForObject(ReviewSql.SELECT_REVIEW_TOTAL_COUNT, (SqlParameterSource) null, Integer.class);
    }

    public int getTotalCount(ReviewSearch reviewSearch){
        StringBuilder sql = new StringBuilder();
        sql.append(ReviewSql.SELECT_REVIEW_TOTAL_COUNT);

        BeanPropertySqlParameterSource beanPropertySqlParameterSource = setCriteriaParamAndSql(sql, reviewSearch);

        return namedParameterJdbcTemplate.queryForObject(sql.toString()
                , beanPropertySqlParameterSource, Integer.class);
    }

    @Override
    public List<Review> findReviews() {
        return namedParameterJdbcTemplate.query(ReviewSql.SELECT_REVIEW, reviewRowMapper);
    }

    @Override
    public List<Review> findReviewsByCriteria(ReviewSearch reviewSearch) {
        StringBuilder sql = new StringBuilder();
        sql.append(ReviewSql.SELECT_REVIEW);

        BeanPropertySqlParameterSource beanPropertySqlParameterSource = setCriteriaParamAndSql(sql, reviewSearch);

        sql.append(ReviewSql.ORDER_BY);

        return namedParameterJdbcTemplate.query(sql.toString(), beanPropertySqlParameterSource, reviewRowMapper);
    }

    @Override
    public int updateReview(Review review) {
        return namedParameterJdbcTemplate.update(ReviewSql.UPDATE_REVIEW, new BeanPropertySqlParameterSource(review));
    }

    @Override
    public int deleteReview(Long reviewId) {
        SqlParameterSource param = new MapSqlParameterSource("reviewId", reviewId);
        return namedParameterJdbcTemplate.update(ReviewSql.DELETE_REVIEW, param);
    }

    private BeanPropertySqlParameterSource setCriteriaParamAndSql(StringBuilder sql, ReviewSearch reviewSearch){
        ReviewSearchForParameter param = new ReviewSearchForParameter();

        param.setOrder(reviewSearch.getOrder().ordinal()+1);
        param.setOffset(reviewSearch.getOffset());
        param.setLimit(reviewSearch.getLimit());

        //memberId
        if(reviewSearch.getMemberId() != null){
            sql.append(ReviewSql.WHERE_MEMBERID);
            param.setMemberId(reviewSearch.getMemberId());
        }
        //title
        if(reviewSearch.getTitle() != null){
            sql.append(ReviewSql.WHERE_TITLE);
            param.setTitle("%"+ reviewSearch.getTitle() + "%");
        }
        //contents
        if(reviewSearch.getContents() != null){
            sql.append(ReviewSql.WHERE_CONTENTS);
            param.setContents("%"+ reviewSearch.getContents() + "%");
        }
        //bookTitle
        if(reviewSearch.getBookTitle() != null){
            sql.append(ReviewSql.WHERE_BOOKTITLE);
            param.setBookTitle("%"+ reviewSearch.getBookTitle() + "%");
        }

        return new BeanPropertySqlParameterSource(param);
    }

    @Getter
    @Setter
    class ReviewSearchForParameter {
        String memberId;
        String title;
        String contents;
        String bookTitle;
        int order;
        int offset;
        int limit;
    }
}
