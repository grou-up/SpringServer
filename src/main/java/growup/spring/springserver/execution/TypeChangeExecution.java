package growup.spring.springserver.execution;

import growup.spring.springserver.execution.domain.Execution;
import growup.spring.springserver.execution.dto.ExecutionMarginResDto;

public class TypeChangeExecution {

    public static ExecutionMarginResDto entityToDto(Execution execution) {
        return ExecutionMarginResDto
                .builder()
                .exeId(execution.getExeId())
                .exeDetailCategory(execution.getExeDetailCategory())
                .exeSalePrice(execution.getExeSalePrice())
                .exeCostPrice(execution.getExeCostPrice())
                .build();
    }

}
