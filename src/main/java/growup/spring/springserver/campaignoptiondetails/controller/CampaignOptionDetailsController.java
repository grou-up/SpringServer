package growup.spring.springserver.campaignoptiondetails.controller;

import growup.spring.springserver.campaignoptiondetails.dto.CampaignOptionDetailsResponseDto;
import growup.spring.springserver.campaignoptiondetails.service.CampaignOptionDetailsService;
import growup.spring.springserver.global.common.CommonResponse;
import growup.spring.springserver.keyword.dto.KeywordResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cod")
public class CampaignOptionDetailsController {

    private final CampaignOptionDetailsService campaignOptionDetailsService;

    @GetMapping("/getMyCampaignDetails")
    public ResponseEntity<CommonResponse<?>> getKeywordAboutCampaign(@RequestParam("start") LocalDate start,
                                                                     @RequestParam("end") LocalDate end,
                                                                     @RequestParam("campaignId") Long campaignId,
                                                                     @AuthenticationPrincipal UserDetails userDetails) {

        List<CampaignOptionDetailsResponseDto> campaignDetailsByCampaignsIds = campaignOptionDetailsService.getCampaignDetailsByCampaignsIds(start, end, campaignId);

        return new ResponseEntity<>(CommonResponse
                .<List<CampaignOptionDetailsResponseDto>>builder("success : getMyCampaignDetails")
                .data(campaignDetailsByCampaignsIds)
                .build(), HttpStatus.OK);

    }
}

