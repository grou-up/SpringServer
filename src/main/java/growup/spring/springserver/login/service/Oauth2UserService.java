package growup.spring.springserver.login.service;

import growup.spring.springserver.global.config.JwtTokenProvider;
import growup.spring.springserver.global.domain.TypeChange;
import growup.spring.springserver.login.domain.Member;
import growup.spring.springserver.login.dto.request.LoginSignUpReqDto;
import growup.spring.springserver.login.repository.MemberRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final TypeChange typeChange;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("Oauth2UserService.loadUser called");

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 클라이언트 ID로 OAuth 공급자 확인 (카카오인지 확인)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if (!"kakao".equals(registrationId)) {
            throw new OAuth2AuthenticationException("Now Only Kakao is supported");
        }

        // 카카오 사용자 정보 처리
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("Kakao user attributes: {}", attributes);

        // 카카오 계정 정보 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = (String) kakaoAccount.get("email");
        String nickname = (String) ((Map<String, Object>) attributes.get("properties")).get("nickname");

        // 기존 사용자 체크
        Member member = memberRepository.findByEmail(email).orElse(null);

        // 기존 사용자가 없으면 새로 생성
        if (member == null) {
            member = typeChange.memberCreateDtoToMember(new LoginSignUpReqDto(email, null, nickname), null);
            memberRepository.save(member);
        }

        // 인증 객체 반환
        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "id"
        );
    }
}
