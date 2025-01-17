package growup.spring.springserver.execution.controller;

import growup.spring.springserver.execution.dto.ExecutionMarginResDto;
import growup.spring.springserver.execution.service.ExecutionService;
import growup.spring.springserver.global.common.CommonResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/execution")
@Slf4j
@AllArgsConstructor
public class ExecutionController {
    private final ExecutionService executionService;

    @GetMapping("/getMyExecutionData")
    public ResponseEntity<CommonResponse<?>> getMyExecutionData(@RequestParam Long campaignId) {
        List<ExecutionMarginResDto> myExecutionData = executionService.getMyExecutionData(campaignId);
        return new ResponseEntity<>(CommonResponse
                .<List<ExecutionMarginResDto>>builder("success : getMyExecutionData")
                .data(myExecutionData)
                .build(), HttpStatus.OK);
    }
}