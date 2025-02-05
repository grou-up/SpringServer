package growup.spring.springserver.global.common;

import growup.spring.springserver.global.config.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // OAuth2User 정보 가져오기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 사용자 정보 추출 (카카오 사용자 정보)
        Optional<Map<String, Object>> kakaoAccountOpt = safeCastToMap(oAuth2User.getAttributes().get("kakao_account"));

        String email = kakaoAccountOpt
                .map(kakaoAccount -> (String) kakaoAccount.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("Kakao account data is missing"));

        Optional<Map<String, Object>> propertiesOpt = safeCastToMap(oAuth2User.getAttributes().get("properties"));

        String nickname = propertiesOpt
                .map(properties -> (String) properties.get("nickname"))
                .orElse("DefaultNickname"); // 대체값을 제공하거나 예외를 던질 수 있음

        String role = "ROLE_USER"; // 기본 사용자 역할

        // JWT 토큰 생성
        String memberSpecification = String.format("%s:%s:%s", email, nickname, role);
        String accessToken = jwtTokenProvider.createAccessToken(memberSpecification);
        log.info("accessToken :{}",accessToken);

        // HTTP 응답 헤더에 토큰 추가
        response.addHeader("Authorization", "Bearer " + accessToken);

        String targetUrl = String.format("https://www.grouup.co.kr/oauth/kakao/callback?token=%s", accessToken);
        log.info("Redirecting to: {}", targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @SuppressWarnings("unchecked")
    private Optional<Map<String, Object>> safeCastToMap(Object obj) {
        if (obj instanceof Map) {
            return Optional.of((Map<String, Object>) obj);
        }
        return Optional.empty();
    }
}