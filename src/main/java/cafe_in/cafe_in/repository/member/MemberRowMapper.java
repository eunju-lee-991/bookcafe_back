package cafe_in.cafe_in.repository.member;

import cafe_in.cafe_in.domain.Member;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MemberRowMapper implements RowMapper<Member> {

    @Override
    public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setNickname(rs.getString("nickname"));
        member.setEmail(rs.getString("email"));
        member.setJoinDate(rs.getTimestamp("joindate").toLocalDateTime());

        return member;
    }

}
