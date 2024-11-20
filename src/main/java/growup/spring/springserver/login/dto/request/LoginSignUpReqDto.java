package growup.spring.springserver.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(title = "MEM_REQ_02 : 회원가입 요청 DTO")
public record LoginSignUpReqDto(
        @NotBlank(message = "사용자 이메일을 입력해주세요")
        @Email(message = "이메일 형식에 맞지 않습니다.")
        @Schema(description = "사용자 이메일", example = "testtest@gmail.com")
        String email,

        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[A-Za-z\\d]{8,20}$|^(?=.*\\d)(?=.*[a-z])(?=.*[!#$%’()*+,./:;?@\\^_`{|}~-])[a-z\\d!#$%’()*+,./:;?@\\^_`{|}~-]{9,20}$|^(?=.*\\d)(?=.*[A-Z])(?=.*[!#$%’()*+,./:;?@\\^_`{|}~-])[A-Z\\d!#$%’()*+,./:;?@\\^_`{|}~-]{9,20}$|^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!#$%’()*+,./:;?@\\^_`{|}~-])[A-Za-z\\d!#$%’()*+,./:;?@\\^_`{|}~-]{9,20}$|^(?=.*[a-z])(?=.*[A-Z])(?=.*[!#$%’()*+,./:;?@\\^_`{|}~-])[A-Za-z!#$%’()*+,./:;?@\\^_`{|}~-]{9,20}$",
                message =  "Invalid password format 비밀번호 정규식 설명필요")
        String password,

        @NotBlank(message = "사용자 이름을 입력해주세요.")
        @Size(min = 3, max = 15, message = "사용자 이름은 15글자 이하로 입력해야 합니다.")
        @Schema(description = "사용자 이름", example = "사용자")
        String name
) {}