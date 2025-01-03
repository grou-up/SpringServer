package growup.spring.springserver.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.Map;


@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> entityNotFoundHandler(EntityNotFoundException e) {
        log.error("Handler EntityNotFoundException message: {}", e.getMessage());
        return ErrorResponseDto.of(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> illegalArgumentHandler(IllegalArgumentException e) {
        log.error("Handler IllegalArgumentException message: {}", e.getMessage());
        return ErrorResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ErrorResponseDto> unsupportedOperationHandler(UnsupportedOperationException e) {
        log.error("Handler UnsupportedOperationException message: {}", e.getMessage());
        return ErrorResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}

