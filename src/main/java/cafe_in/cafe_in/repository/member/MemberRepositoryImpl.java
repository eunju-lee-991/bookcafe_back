package cafe_in.cafe_in.repository.member;

import cafe_in.cafe_in.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final MemberRowMapper memberRowMapper;

    public void join(Member member) {
        //회원가입
        namedParameterJdbcTemplate.update(MemberSql.INSERT_MEMBER, new BeanPropertySqlParameterSource(member));
    }

    public List<Member> findMembers() {
        return namedParameterJdbcTemplate.query(MemberSql.SELECT_MEMBER_ALL, memberRowMapper);
    }

//아직 테스트중 미완성
    public List<Member> findMembersByCriteria(MemberSearch memberSearch) {
        //SqlParameterSource param = new BeanPropertySqlParameterSource(memberSearch);
//tesettesettesttestttttttt
        SqlParameterSource param = new MapSqlParameterSource("order", memberSearch.getOrder().ordinal() + 1);
        log.info("order: {} ", memberSearch.getOrder().ordinal() + 1);

        //order by 할 때는 컬럼이름을 숫자로..
        return namedParameterJdbcTemplate.query(MemberSql.SELECT_MEMBER_ALL_ORDER, param, memberRowMapper);
    }

    public Optional<Member> findOne(Long id) {
        SqlParameterSource param = new MapSqlParameterSource("id", id);

        /*  Member member = namedParameterJdbcTemplate.queryForObject(MemberSql.SELECT_MEMBER_BY_ID, param, Member.class);
        queryForObject는 하나의 행이 반환되지 않는 경우 예외 발생
        그냥 .query 실행해서 list 가져와서 처리 */

        return Optional.ofNullable(DataAccessUtils.uniqueResult(namedParameterJdbcTemplate.query(MemberSql.SELECT_MEMBER_BY_ID, param, memberRowMapper)));
    }

    public int deleteOne(Long id) {
        SqlParameterSource param = new MapSqlParameterSource("id", id);

        return namedParameterJdbcTemplate.update(MemberSql.DELETE_MEMBER, param);
    }
}
