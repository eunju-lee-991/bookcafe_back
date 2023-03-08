package cafe_in.cafe_in.exception;

import cafe_in.cafe_in.domain.Member;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.Iterator;

@RestController
@ControllerAdvice
public class CustomizdResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(HttpStatus httpStatus, Exception ex) {
        ExceptionResponse exceptionResponse
                = new ExceptionResponse(httpStatus.value(), ex.getMessage());

        return ResponseEntity.status(httpStatus).body(exceptionResponse);
    }

    public final ResponseEntity<Object> handleAllExceptions(HttpStatus httpStatus, String message) {
        ExceptionResponse exceptionResponse
                = new ExceptionResponse(httpStatus.value(), message);

        return ResponseEntity.status(httpStatus).body(exceptionResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Object> handleRuntimeException(Exception ex) {
        return handleAllExceptions(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(BindingFieldFailException.class)
    public final ResponseEntity<Object> handleBindingFieldFailException(Exception ex) {
        return handleAllExceptions(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public final ResponseEntity<Object> handleDuplicateKeyException(Exception ex) {
        String message = ex.getCause().getMessage().substring(0, ex.getCause().getMessage().indexOf(":")); // Unique index or primary key violation
        return handleAllExceptions(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public final ResponseEntity<Object> handleMemberNotFoundException(Exception ex) {
        return handleAllExceptions(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public final ResponseEntity<Object> handleReviewNotFoundException(Exception ex, WebRequest request) {
        return handleAllExceptions(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(AuthorizationCodeNotFoundException.class)
    public final ResponseEntity<Object> handleAuthorizationCodeNotFoundException(Exception ex, WebRequest request) {
        return handleAllExceptions(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public final ResponseEntity<Object> handleDuplicateUserException(Exception ex, WebRequest request) {
        return handleAllExceptions(HttpStatus.BAD_REQUEST, ex);
    }
}
