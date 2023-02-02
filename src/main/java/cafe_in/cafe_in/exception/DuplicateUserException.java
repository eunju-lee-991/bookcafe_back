package cafe_in.cafe_in.exception;

import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateUserException extends RuntimeException { // DuplicateMemberException 이라는 예외가 이미 존재해서 User로 클래스 이름
    public DuplicateUserException(String message) {
        super(message);
    }
}
