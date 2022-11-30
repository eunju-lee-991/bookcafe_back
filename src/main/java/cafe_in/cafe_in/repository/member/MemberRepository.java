package cafe_in.cafe_in.repository.member;

import cafe_in.cafe_in.controller.MemberKakaoForm;
import cafe_in.cafe_in.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final MemberRowMapper memberRowMapper;

    public int save(MemberKakaoForm form) {
        //회원가입 만들기
        SqlParameterSource param = new BeanPropertySqlParameterSource(form); // 쿼리 파라미터 대소문자 구분해야함

        return namedParameterJdbcTemplate.update(MemberSql.INSERT_MEMBER, param);
    }

    public List<Member> findMemberById(Long id) {

        SqlParameterSource param = new MapSqlParameterSource("id", id); // 쿼리 파라미터 대소문자 구분해야함

        List<Member> list = namedParameterJdbcTemplate.query(MemberSql.SELECT_MEMBER_BY_ID, param, memberRowMapper);


      /*  Member member = namedParameterJdbcTemplate.queryForObject(MemberSql.SELECT_MEMBER_BY_ID, param, Member.class);
        이렇게 하려면 무조건 하나의 행이 반환되야 하는군
        그냥 .query 실행해서 list 가져와서 처리해주는 게 깔끔*/


        for (Member m : list) {
            log.info("zzzz.. "+ m.getId() + m.getEmail());
        }

        return list;
    }
}
