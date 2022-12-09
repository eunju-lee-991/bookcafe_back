package cafe_in.cafe_in.repository.member;

import cafe_in.cafe_in.controller.MemberKakaoForm;
import cafe_in.cafe_in.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final MemberRowMapper memberRowMapper;

    public Long join(MemberKakaoForm form) {
        //회원가입
        SqlParameterSource param = new BeanPropertySqlParameterSource(form); // * 쿼리 파라미터 대소문자 구분해야함
        namedParameterJdbcTemplate.update(MemberSql.INSERT_MEMBER, param);
        return form.getId();
    }

    public List<Member> findMembers() {
        return namedParameterJdbcTemplate.query(MemberSql.SELECT_MEMBER_ALL, memberRowMapper);
    }

    public Optional<Member> findOne(Long id) {
        SqlParameterSource param = new MapSqlParameterSource("id", id);

        /*  Member member = namedParameterJdbcTemplate.queryForObject(MemberSql.SELECT_MEMBER_BY_ID, param, Member.class);
        queryForObject는 하나의 행이 반환되지 않는 경우 예외 발생
        그냥 .query 실행해서 list 가져와서 처리 */

        return Optional.ofNullable(DataAccessUtils.uniqueResult(namedParameterJdbcTemplate.query(MemberSql.SELECT_MEMBER_BY_ID, param, memberRowMapper)));
    }

}
