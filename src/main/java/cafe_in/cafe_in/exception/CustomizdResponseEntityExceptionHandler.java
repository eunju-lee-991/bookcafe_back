package cafe_in.cafe_in.exception;

import cafe_in.cafe_in.domain.Member;
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
@ControllerAdvice // 모든 컨트롤러가 실행될 때 사전에 실행
public class CustomizdResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request, @Nullable HttpStatus httpStatus) {
        ExceptionResponse exceptionResponse
                = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, httpStatus);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public final ResponseEntity<Object> handleMemberNotFoundException(Exception ex, WebRequest request) {
        return handleAllExceptions(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public final ResponseEntity<Object> handleDuplicateUserException(Exception ex, WebRequest request) {
        return handleAllExceptions(ex, request, HttpStatus.BAD_REQUEST);
    }

}
