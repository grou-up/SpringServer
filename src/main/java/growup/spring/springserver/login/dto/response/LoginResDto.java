package growup.spring.springserver.login.dto.response;

import lombok.Builder;

@Builder
public record LoginResDto(
    String email,
    String name,
    String accessToken
)
{ }