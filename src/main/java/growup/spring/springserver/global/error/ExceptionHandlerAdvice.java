package growup.spring.springserver.global.error;

import growup.spring.springserver.exception.InvalidDateFormatException;
import growup.spring.springserver.exception.campaign.CampaignNotFoundException;
import growup.spring.springserver.global.exception.GrouException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(CampaignNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> campaignNotFoundException(CampaignNotFoundException e) {
        log.error("Handler campaignNotFoundException message: {}", e.getErrorCode());
        return ErrorResponseDto.of(HttpStatus.BAD_REQUEST, e.getErrorCode().getMessage());
    }

    @ExceptionHandler(GrouException.class)
    public ResponseEntity<ErrorResponseDto> campaignNotFoundException(GrouException e) {
        log.error("Handler campaignNotFoundException message: {}", e.getErrorCode());
        return ErrorResponseDto.of(HttpStatus.BAD_REQUEST, e.getErrorCode().getMessage());
    }

    // controller 에서 LocalDate Parm 형식 오류 시 던지는 에러
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> MethodArgumentTypeMismatchException() {
        InvalidDateFormatException e = new InvalidDateFormatException();
        log.error("Handler campaignNotFoundException message: {}", e.getErrorCode());
        return ErrorResponseDto.of(HttpStatus.BAD_REQUEST, e.getErrorCode().getMessage());
    }

}

