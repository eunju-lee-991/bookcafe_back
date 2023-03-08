package cafe_in.cafe_in.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class AuthorizationCodeNotFoundException extends RuntimeException {
    public AuthorizationCodeNotFoundException(String message) {super(message);}
}
