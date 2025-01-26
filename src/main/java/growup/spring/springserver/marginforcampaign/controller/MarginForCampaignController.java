package growup.spring.springserver.marginforcampaign.controller;

import growup.spring.springserver.global.common.CommonResponse;
import growup.spring.springserver.marginforcampaign.dto.MarginForCampaignResDto;
import growup.spring.springserver.marginforcampaign.dto.MfcRequestDtos;
import growup.spring.springserver.marginforcampaign.dto.MfcValidationResponseDto;
import growup.spring.springserver.marginforcampaign.service.MarginForCampaignService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/updateExecutionAboutCampaign")
    public ResponseEntity<CommonResponse<MfcValidationResponseDto>> updateExecutionAboutCampaign(@Valid @RequestBody MfcRequestDtos mfcRequestDtos,
                                                                                                 @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        MfcValidationResponseDto mfcValidationResponseDto = marginForCampaignService.searchMarginForCampaignProductName(email, mfcRequestDtos);

        return new ResponseEntity<>(CommonResponse
                .<MfcValidationResponseDto>builder("success : getExecutionAboutCampaign")
                .data(mfcValidationResponseDto)
                .build(), HttpStatus.OK);
    }
}