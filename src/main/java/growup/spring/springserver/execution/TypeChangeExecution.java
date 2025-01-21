package growup.spring.springserver.execution;

import growup.spring.springserver.execution.domain.Execution;
import growup.spring.springserver.execution.dto.ExecutionResponseDto;

public class TypeChangeExecution {
    public static ExecutionResponseDto entityToDto(Execution e){
        return ExecutionResponseDto.builder()
                .exeId(e.getExeId())
                .exeProductName(e.getExeProductName())
                .build();
    }
}
