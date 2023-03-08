package cafe_in.cafe_in.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BindingFieldFailException extends RuntimeException {
    public BindingFieldFailException(FieldError fieldError) {
            super("오류 발생 필드명: " + fieldError.getField() + "[" + fieldError.getDefaultMessage() + ".]");
    }

    public BindingFieldFailException(String message) {
        super(message);
    }
}
