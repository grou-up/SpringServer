package growup.spring.springserver.marginforcampaign.controller;

import growup.spring.springserver.execution.dto.ExecutionMarginResDto;
import growup.spring.springserver.global.common.CommonResponse;
import growup.spring.springserver.marginforcampaign.dto.MarginForCampaignResDto;
import growup.spring.springserver.marginforcampaign.service.MarginForCampaignService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/marginforcam")
@AllArgsConstructor
public class MarginForCampaignController {
    private final MarginForCampaignService marginForCampaignService;

    @GetMapping("/getExecutionAboutCampaign")
    public ResponseEntity<CommonResponse<List<MarginForCampaignResDto>>> getExecutionAboutCampaign(@RequestParam Long campaignId) {
        List<MarginForCampaignResDto> marginForCampaignResDtos = marginForCampaignService.marginForCampaignByCampaignId(campaignId);

        return new ResponseEntity<>(CommonResponse
                .<List<MarginForCampaignResDto>>builder("success : getExecutionAboutCampaign")
                .data(marginForCampaignResDtos)
                .build(), HttpStatus.OK);
    }
}