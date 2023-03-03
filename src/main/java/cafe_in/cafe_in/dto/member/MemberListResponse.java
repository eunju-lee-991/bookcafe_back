package cafe_in.cafe_in.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MemberListResponse {
    private int count;
    private List<MemberDto> members;
}
