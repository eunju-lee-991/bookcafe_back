package cafe_in.cafe_in.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Slf4j
public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String message, String access_token) {
        super(message);
        log.info("this access token does not exist: {}", access_token);
    }
}
