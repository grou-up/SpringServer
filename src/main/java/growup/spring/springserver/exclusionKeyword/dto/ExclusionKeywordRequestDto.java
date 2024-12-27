package growup.spring.springserver.exclusionKeyword.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@Builder
public class ExclusionKeywordRequestDto {
    @NotNull(message = "조회할 캠패인 ID를 입력해주세요")
    Long campaignId;
    @NotBlank(message = "제외 키워드를 입력해주세요")
    String exclusionKeyword;
}
