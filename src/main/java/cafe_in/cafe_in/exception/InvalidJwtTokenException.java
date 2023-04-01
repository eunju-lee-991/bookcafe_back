package cafe_in.cafe_in.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidJwtTokenException extends RuntimeException{
    public InvalidJwtTokenException(String token) {
        super("This JWT token is invalid. Token: " + token);
    }
}
