package growup.spring.springserver.execution.controller;

import growup.spring.springserver.exception.RequestError;
import growup.spring.springserver.execution.domain.Execution;
import growup.spring.springserver.execution.dto.ExecutionResponseDto;
import growup.spring.springserver.execution.service.ExecutionService;
import growup.spring.springserver.global.common.CommonResponse;
import growup.spring.springserver.execution.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/execution")
@RestController
public class ExecutionController {
    @Autowired
    private ExecutionService executionService;

    @GetMapping("/getByCampaignIdAndExeIds")
    public ResponseEntity<CommonResponse<?>> getByCampaignIdAndExeIds(
            @RequestParam("campaignId") Long campaignId,
            @RequestParam("exeIds") List<Long> exeIds) throws RequestError {

        if (exeIds == null || exeIds.isEmpty() || campaignId == null) {
            throw new RequestError();
        }

        List<Execution> result = executionService.getExecutionsByCampaignIdAndExeIds(campaignId, exeIds);
        return new ResponseEntity<>(CommonResponse
                .<List<ExecutionResponseDto>>builder("get Api success")
                .data(result.stream().map(TypeChangeExecution::entityToDto).toList())
                .build(), HttpStatus.OK);
    }
}
