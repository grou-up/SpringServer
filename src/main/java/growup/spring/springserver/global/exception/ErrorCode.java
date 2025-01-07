package growup.spring.springserver.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //Login
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    MEMBER_KAKAO_LOGIN_FOUND(HttpStatus.BAD_REQUEST,"해당 계정은 카카오 로그인 전용입니다."),
    MEMBER_INVALID_PASSWORD_EXCEPTION(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),

    // Campaign
    CAMPAIGN_NOT_FOUND(HttpStatus.NOT_FOUND, "현재 등록된 캠페인이 없습니다."),

    // Keyword
//    KEYWORD_
    //Global
//    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST,"날짜 형식이 이상합니다."),

    ;


    ErrorCode(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }

    private HttpStatus httpStatus;
    private String message;

}
