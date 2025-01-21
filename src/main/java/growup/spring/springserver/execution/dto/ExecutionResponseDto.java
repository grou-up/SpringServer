package growup.spring.springserver.execution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionResponseDto {
    private Long exeId;
    private String exeProductName;
}
