package growup.spring.springserver.global.oauth;

import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Map;
import java.util.Optional;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;

    public KakaoOAuth2UserInfo(OAuth2User oAuth2User) {
        this.attributes = oAuth2User.getAttributes();
    }

    @Override
    public String getEmail() {
        return safeCastToMap(attributes.get("kakao_account"))
                .map(account -> (String) account.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("Kakao account data is missing"));
    }

    @Override
    public String getNickname() {
        return safeCastToMap(attributes.get("properties"))
                .map(properties -> (String) properties.get("nickname"))
                .orElse("DefaultNickname");
    }

    @SuppressWarnings("unchecked")
    private Optional<Map<String, Object>> safeCastToMap(Object obj) {
        if (obj instanceof Map) {
            return Optional.of((Map<String, Object>) obj);
        }
        return Optional.empty();
    }
}
