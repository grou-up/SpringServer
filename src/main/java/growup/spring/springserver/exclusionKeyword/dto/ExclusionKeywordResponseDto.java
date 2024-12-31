package growup.spring.springserver.exclusionKeyword.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ExclusionKeywordResponseDto {
    String exclusionKeyword;
    Long campaignId;
    LocalDate addTime;
}
