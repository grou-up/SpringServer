package growup.spring.springserver.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class ExceptionPriorityHandler {

    @ExceptionHandler({BindException.class})
    @Order(value = Ordered.HIGHEST_PRECEDENCE) // 최우선 순위
    public ResponseEntity<Map<String, Object>> handleBindException(BindException ex) {
        // 에러 메시지 추출
        List<String> errorMessages = ex.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList(); // add java 16 , before 16 .collect(Collectors.toList());


        // 여러 에러 메시지를 하나의 문자열로 합치기 (콤마로 구분)
        String combinedErrorMessage = String.join(", ", errorMessages);

        // ErrorResponseDto를 통해 응답 생성
        return ErrorResponseDto.makeMessage(HttpStatus.BAD_REQUEST, combinedErrorMessage);
    }
}
