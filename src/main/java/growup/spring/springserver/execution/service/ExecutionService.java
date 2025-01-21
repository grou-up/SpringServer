package growup.spring.springserver.execution.service;

import growup.spring.springserver.execution.domain.Execution;
import growup.spring.springserver.execution.repository.ExecutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExecutionService {
    @Autowired
    private ExecutionRepository executionRepository;

    public List<Execution> getExecutionsByCampaignIdAndExeIds(Long campaignId, List<Long> exeIds){
        List<Execution> result = executionRepository.findByCampaignIdAndExeId(campaignId,exeIds);
        return result;
    }
}
