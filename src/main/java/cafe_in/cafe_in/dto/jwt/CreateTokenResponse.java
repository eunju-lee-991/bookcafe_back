package cafe_in.cafe_in.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@AllArgsConstructor
public class CreateTokenResponse {
    public String accessToken;
    public String refreshToken;
    public Date accessTokenExp;
    public Date refreshTokenExp;
}
