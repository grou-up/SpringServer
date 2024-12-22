package growup.spring.springserver.campaign;

import growup.spring.springserver.global.common.CommonResponse;
import growup.spring.springserver.login.dto.response.LoginResDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
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
        List<String> data = campaignService.getMyCampaigns(userDetails.getUsername());
        log.info("End getMyCampaigns API target is :"+userDetails.getUsername());
        return new ResponseEntity<>(CommonResponse
                .<List<String>>builder("success : load campaign name list")
                .data(data)
                .build(), HttpStatus.OK);
    }
}
