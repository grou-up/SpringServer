package growup.spring.springserver.margin.controller;

import growup.spring.springserver.global.common.CommonResponse;
import growup.spring.springserver.margin.dto.MarginSummaryResponseDto;
import growup.spring.springserver.margin.service.MarginService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/margin")
@Slf4j
@AllArgsConstructor
public class MarginController {
    private final MarginService marginService;

    @GetMapping("/getCampaignAllSales")
    public ResponseEntity<CommonResponse<?>> getCampaignAllSales(@RequestParam("date") LocalDate date,
                                                                 @AuthenticationPrincipal UserDetails userDetails) {
        List<MarginSummaryResponseDto> campaignAllSales = marginService.getCampaignAllSales(userDetails.getUsername(), date);

        return new ResponseEntity<>(CommonResponse
                .<List<MarginSummaryResponseDto>>builder("success : getMyCampaignDetails")
                .data(campaignAllSales)
                .build(), HttpStatus.OK);
    }
}