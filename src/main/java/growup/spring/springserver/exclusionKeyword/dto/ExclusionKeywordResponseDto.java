package growup.spring.springserver.exclusionKeyword.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExclusionKeywordResponseDto {
    String exclusionKeyword;
    Long campaignId;
}
