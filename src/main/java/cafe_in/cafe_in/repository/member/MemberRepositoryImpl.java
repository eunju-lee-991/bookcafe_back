package cafe_in.cafe_in.repository.member;

import cafe_in.cafe_in.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
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
public class MemberRepositoryImpl implements MemberRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final MemberRowMapper memberRowMapper;

    public void join(Member member) {
        //회원가입
        namedParameterJdbcTemplate.update(MemberSql.INSERT_MEMBER, new BeanPropertySqlParameterSource(member)); //BeanPropertySqlParameterSource: Map 객체로 변환시켜주는 객체
    }

    public Optional<Member> findOne(Long id) {
        String findOneSql = MemberSql.SELECT_MEMBER + MemberSql.WHERE_ID;
        SqlParameterSource param = new MapSqlParameterSource("id", id);

        /*  Member member = namedParameterJdbcTemplate.queryForObject(MemberSql.SELECT_MEMBER_BY_ID, param, Member.class); 를 사용하지 않고 List 타입으로 반환하는 .query를 쓰는 이유
        queryForObject는 하나의 행이 반환되지 않는 경우(id에 해당하는 로우가 없는 경우) 예외가 발생
        그냥 .query 실행해서 list로 가져와서 uniqueResult 처리 */
        return Optional.ofNullable(DataAccessUtils.uniqueResult(namedParameterJdbcTemplate.query(findOneSql, param, memberRowMapper)));
        //DataAccessUtils.uniqueResult를 사용하면 결과가 0일 때 null로 만들어주기 때문에 optional로 감싸줌
    }

    public List<Member> findMembers() {
        return namedParameterJdbcTemplate.query(MemberSql.SELECT_MEMBER, memberRowMapper);
    }

    public List<Member> findMembersByCriteria(MemberSearch memberSearch) {
        String findMembersByCriteriaSql = MemberSql.SELECT_MEMBER;
        MemberSearchForParameter param = new MemberSearchForParameter();

        param.setOrder(memberSearch.getOrder().ordinal() + 1); //order by 할 때는 컬럼 순서를 넣어줘야해서 MemberSearchOrder를 숫자로 변경

        log.info("orderBy: {}, {}번째 컬럼", memberSearch.getOrder(), memberSearch.getOrder().ordinal() + 1);

        if (memberSearch.getNickname() != null) {
            findMembersByCriteriaSql += MemberSql.WHERE_NICKNAME;
            param.setNickname("%" + memberSearch.getNickname()+ "%");
        }

        if (memberSearch.getEmail() != null) {
            findMembersByCriteriaSql += MemberSql.WHERE_EMAIL;
            param.setEmail("%" + memberSearch.getEmail() + "%");
        }

        findMembersByCriteriaSql += MemberSql.ORDER_BY;

        BeanPropertySqlParameterSource beanParam = new BeanPropertySqlParameterSource(param);

        return namedParameterJdbcTemplate.query(findMembersByCriteriaSql, beanParam, memberRowMapper);
    }

    @Override
    public int updateMember(Member member) {
        return namedParameterJdbcTemplate.update(MemberSql.UPDATE_MEMBER, new BeanPropertySqlParameterSource(member));
    }

    public int deleteOne(Long id) {
        SqlParameterSource param = new MapSqlParameterSource("id", id);

        return namedParameterJdbcTemplate.update(MemberSql.DELETE_MEMBER, param);
    }

    @Getter
    @Setter
    class MemberSearchForParameter {
        String nickname;
        String email;
        int order;
    }
}
