package growup.spring.springserver.execution.service;

import growup.spring.springserver.execution.TypeChangeExecution;
import growup.spring.springserver.execution.domain.Execution;
import growup.spring.springserver.execution.dto.ExecutionMarginResDto;
import growup.spring.springserver.execution.repository.ExecutionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ExecutionService {
    private final ExecutionRepository executionRepository;

    public List<ExecutionMarginResDto> getMyExecutionData(Long campaignId) {
        List<Execution> executionIdsByCampaignId = executionRepository.findExecutionByCampaignId(campaignId);

        List<ExecutionMarginResDto> data = new ArrayList<>();
        for (Execution execution : executionIdsByCampaignId) {
            data.add(
                    TypeChangeExecution.entityToDto(execution)
            );
        }
        return data;
    }
}
