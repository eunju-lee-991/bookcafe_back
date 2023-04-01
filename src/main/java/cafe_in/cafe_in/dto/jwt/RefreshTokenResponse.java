package cafe_in.cafe_in.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@Builder
public class RefreshTokenResponse {
    public String accessToken;
    public Date accessTokenExp;
}
