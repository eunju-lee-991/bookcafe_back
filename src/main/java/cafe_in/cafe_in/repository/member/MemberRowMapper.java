package cafe_in.cafe_in.repository.member;

import cafe_in.cafe_in.domain.Member;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class MemberRowMapper implements RowMapper<Member> {

    @Override
    public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
        Member member = new Member();
        member.setId(rs.getLong("ID"));
        member.setNickname(rs.getString("NICKNAME"));
        member.setEmail(rs.getString("EMAIL"));
        member.setJoinDate(rs.getTimestamp("JOINDATE").toLocalDateTime());

        return member;
    }

}
