package cafe_in.cafe_in.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
public class ExceptionResponse { //나중에 삭제
    private int statusCode;
    private String message;
}
