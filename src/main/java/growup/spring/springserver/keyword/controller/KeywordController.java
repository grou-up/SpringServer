package growup.spring.springserver.keyword.controller;

import growup.spring.springserver.global.common.CommonResponse;
import growup.spring.springserver.keyword.dto.KeywordResponseDto;
import growup.spring.springserver.keyword.service.KeywordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/keyword")
@RestController
@Slf4j
public class KeywordController {
    @Autowired
    private KeywordService keywordService;

    @GetMapping("/getKeywordsAboutCampaign")
    public ResponseEntity<CommonResponse<?>> getKeywordAboutCampaign(@RequestParam("start") String start,
                                                                     @RequestParam("end") String end,
                                                                     @RequestParam("campaignId") Long campaignId,
                                                                     @AuthenticationPrincipal UserDetails userDetails){
        log.info("start getKeywordAboutCampaign target "+ userDetails.getUsername());
        List<KeywordResponseDto> result = keywordService.getKeywordsByCampaignId(start,end,campaignId);
        log.info("end getKeywordAboutCampaign target "+ userDetails.getUsername());
        return new ResponseEntity<>(CommonResponse
                .<List<KeywordResponseDto>>builder("success : get keywords")
                .data(result)
                .build(), HttpStatus.OK);
    }
}
