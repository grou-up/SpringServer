package growup.spring.springserver.campaign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
public class CampaignController {
    @Autowired
    private CampaignService campaignService;

    @GetMapping("/campaign/getMyCampaigns")
    public String getMyCampaigns(@AuthenticationPrincipal UserDetails userDetails) {
        List<String> data = campaignService.getMyCampaigns(userDetails.getUsername());
        // 추가 로직 수행
        return "OK";
    }
}
