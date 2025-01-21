package growup.spring.springserver.execution.controller;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.service.CampaignService;
import growup.spring.springserver.execution.dto.ExecutionMarginResDto;
import growup.spring.springserver.execution.dto.ExecutionRequestDtos;
import growup.spring.springserver.execution.dto.ExecutionResponseDto;
import growup.spring.springserver.execution.service.ExecutionService;
import growup.spring.springserver.global.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/execution")
@AllArgsConstructor
public class ExecutionController {
    private final ExecutionService executionService;
    private final CampaignService campaignService;
    @GetMapping("/getMyExecutionData")
    public ResponseEntity<CommonResponse<List<ExecutionMarginResDto>>> getMyExecutionData(@RequestParam Long campaignId) {
        List<ExecutionMarginResDto> myExecutionData = executionService.getMyExecutionData(campaignId);
        return new ResponseEntity<>(CommonResponse
                .<List<ExecutionMarginResDto>>builder("success : getMyExecutionData")
                .data(myExecutionData)
                .build(), HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<CommonResponse<ExecutionResponseDto>> updateMyExecutionData(@AuthenticationPrincipal UserDetails userDetails,
                                                                                      @Valid @RequestBody ExecutionRequestDtos executionRequestDtos) {
        Campaign campaign = campaignService.getMyCampaign(executionRequestDtos.getCampaignId(), userDetails.getUsername());

        ExecutionResponseDto executionResponseDto = executionService.updateExecutions(executionRequestDtos, campaign);

        return new ResponseEntity<>(CommonResponse
                .<ExecutionResponseDto>builder("success : update execution data")
                .data(executionResponseDto)
                .build(), HttpStatus.OK);
    }
}