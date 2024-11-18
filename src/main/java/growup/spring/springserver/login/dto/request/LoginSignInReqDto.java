package growup.spring.springserver.login.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginSignInReqDto(
        @NotEmpty
        @Email
        String email,

        @NotEmpty
        String password
        ) {
}
