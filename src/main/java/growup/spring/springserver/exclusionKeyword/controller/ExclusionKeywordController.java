package growup.spring.springserver.exclusionKeyword.controller;

import growup.spring.springserver.exclusionKeyword.dto.ExclusionKeywordRequestDto;
import growup.spring.springserver.exclusionKeyword.dto.ExclusionKeywordResponseDto;
import growup.spring.springserver.exclusionKeyword.service.ExclusionKeywordService;
import growup.spring.springserver.global.common.CommonResponse;
import growup.spring.springserver.keyword.dto.KeywordResponseDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/exclusionKeyword")
@RestController
@Slf4j
public class ExclusionKeywordController {
    @Autowired
    private ExclusionKeywordService exclusionKeywordService;

    @PostMapping("/addExclusionKeyword")
    public ResponseEntity<CommonResponse<?>> addExclusionKeyword(@Valid @RequestBody ExclusionKeywordRequestDto requestDto, BindingResult bindingResult) throws BindException {
//        log.info("start signup");
        if (bindingResult.hasErrors()) {
            log.info("bindExceptions is throw");
            throw new BindException(bindingResult);
        }
        final ExclusionKeywordResponseDto result = exclusionKeywordService.addExclusionKeyword(requestDto.getCampaignId(),requestDto.getExclusionKeyword());
        return new ResponseEntity<>(CommonResponse
                .<ExclusionKeywordResponseDto>builder("success : add exclusionKeyword")
                .data(result)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CommonResponse<?>> removeExclusionKeyword(@RequestParam(value ="campaignId") Long campaignId,
                                                                    @RequestParam(value = "exclusionKeyword") String exclusionKeyword) {
        final boolean result = exclusionKeywordService.deleteExclusionKeyword(campaignId,exclusionKeyword);
        return new ResponseEntity<>(CommonResponse
                .<Boolean>builder("success : remove")
                .data(result)
                .build(), HttpStatus.OK);
    }


}
