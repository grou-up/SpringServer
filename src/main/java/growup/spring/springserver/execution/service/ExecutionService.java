package growup.spring.springserver.execution.service;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.exception.campaignoptiondetails.CampaignOptionNotFoundException;
import growup.spring.springserver.execution.TypeChangeExecution;
import growup.spring.springserver.execution.domain.Execution;
import growup.spring.springserver.execution.dto.ExecutionDto;
import growup.spring.springserver.execution.dto.ExecutionMarginResDto;
import growup.spring.springserver.execution.dto.ExecutionRequestDtos;
import growup.spring.springserver.execution.dto.ExecutionResponseDto;
import growup.spring.springserver.execution.repository.ExecutionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
  
    public List<Execution> getExecutionsByCampaignIdAndExeIds(Long campaignId, List<Long> exeIds) {
        List<Execution> result = executionRepository.findByCampaignIdAndExeId(campaignId, exeIds);
        return result;
    }
  
    @Transactional
    public ExecutionResponseDto updateExecutions(ExecutionRequestDtos requests, Campaign campaign) {
        int requestDataSize = requests.getData().size();

        List<Execution> check = check(requests.getData(), campaign);
        return TypeChangeExecution.entityToDto(requestDataSize, check.size());
    }

    public List<Execution> check(List<ExecutionDto> datas, Campaign campaign) {
        List<Execution> list = new ArrayList<>();
        for (ExecutionDto data : datas) {
            try {
                Execution execution = getExecution(campaign, data);
                execution.update(data.getExeSalePrice(), data.getExeTotalPrice(), data.getExeCostPrice(), data.getExePerPiece(), data.getExeZeroRoas());
                list.add(execution);
            } catch (CampaignOptionNotFoundException error) {
                continue;
            }
        }
        return list;
    }

    private Execution getExecution(Campaign campaign, ExecutionDto data) {
        return executionRepository.findByCampaign_CampaignIdAndExeId(campaign.getCampaignId(), data.getExecutionId()).orElseThrow(
                CampaignOptionNotFoundException::new
        );
    }
}
