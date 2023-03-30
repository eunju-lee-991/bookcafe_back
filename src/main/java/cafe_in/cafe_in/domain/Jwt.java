package cafe_in.cafe_in.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter @Setter
public class Jwt {
    public String accessToken;
    public String refreshToken;
    public Long id;
    public Date accessTokenExp;
    public Date refreshTokenExp;
}
