package growup.spring.springserver.execution.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ExecutionRequestDtos {
    private Long campaignId;

    private List<ExecutionDto> data;
}