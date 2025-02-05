package growup.spring.springserver.campaign.controller;

import growup.spring.springserver.campaign.dto.CampaignResponseDto;
import growup.spring.springserver.campaign.service.CampaignService;
import growup.spring.springserver.global.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/api/campaign")
@RestController
public class CampaignController {
    @Autowired
    private CampaignService campaignService;

    @GetMapping("/getMyCampaigns")
    public ResponseEntity<CommonResponse<?>>getMyCampaigns(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("Start getMyCampaigns API target is :"+userDetails.getUsername());
        List<CampaignResponseDto> data = campaignService.getMyCampaigns(userDetails.getUsername());
        log.info("End getMyCampaigns API target is :"+userDetails.getUsername());
        return new ResponseEntity<>(CommonResponse
                .<List<CampaignResponseDto>>builder("success : load campaign name list")
                .data(data)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/test")
    public String check() {
        return "OK";
    }
}
