package growup.spring.springserver.global.domain;

import growup.spring.springserver.global.support.Role;
import growup.spring.springserver.login.domain.Member;
import growup.spring.springserver.login.dto.request.LoginSignUpReqDto;
import growup.spring.springserver.login.dto.response.LoginResDto;
import org.springframework.stereotype.Component;

// reqdto, resdto change
@Component
public class TypeChange {

    // 멤버 객체 생성
    public Member memberCreateDtoToMember(LoginSignUpReqDto loginCreateReqDto, String encode) {
        return Member.builder()
                .email(loginCreateReqDto.email())  // record에서 email 필드는 email()으로 접근
                .password(encode)  // password()로 접근
                .name(loginCreateReqDto.name())
                .role(Role.USER)
                .build();
    }
    // 로그인 및 토큰 발행
    public LoginResDto memberToLoginResDto(Member findmember, String accessToken) {
        return LoginResDto.builder()
                .email(findmember.getEmail())
                .name(findmember.getName())
                .accessToken(accessToken)
                .build();
    }
}