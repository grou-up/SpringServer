package growup.spring.springserver.exclusionKeyword.service;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.repository.CampaignRepository;
import growup.spring.springserver.campaign.service.CampaignService;
import growup.spring.springserver.exception.exclusionKeyword.ExclusionKeyNotFound;
import growup.spring.springserver.exception.exclusionKeyword.ExistExclusionKeyword;
import growup.spring.springserver.exclusionKeyword.TypeChangeExclusionKeyword;
import growup.spring.springserver.exclusionKeyword.domain.ExclusionKeyword;
import growup.spring.springserver.exclusionKeyword.dto.ExclusionKeywordRequestDto;
import growup.spring.springserver.exclusionKeyword.dto.ExclusionKeywordResponseDto;
import growup.spring.springserver.exclusionKeyword.repository.ExclusionKeywordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExclusionKeywordService {
    @Autowired
    private ExclusionKeywordRepository exclusionKeywordRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CampaignService campaignService;

    public ExclusionKeywordResponseDto addExclusionKeyword(ExclusionKeywordRequestDto exclusionKeywordRequestDto,String email){
        Campaign campaign = campaignService.getMyCampaign(exclusionKeywordRequestDto.getCampaignId(),email);
        int addSuccess = 0;
        int addFail = 0;
        for(String keyword : exclusionKeywordRequestDto.getExclusionKeyword()){
            try {
                saveNoExistExclusionKeyword(keyword,campaign);
                addSuccess++;
            }catch (ExistExclusionKeyword e){
                addFail ++;
            }
        }
        return ExclusionKeywordResponseDto.builder()
                .responseData(addSuccess)
                .requestData(addFail+addSuccess)
                .build();
    }
    public void saveNoExistExclusionKeyword(String keyword,Campaign campaign){
        if(exclusionKeywordRepository.existsByExclusionKeyword(keyword))
            throw new ExistExclusionKeyword();
        ExclusionKeyword exclusionKeyword = ExclusionKeyword.builder()
                .exclusionKeyword(keyword)
                .campaign(campaign)
                .build();
        exclusionKeywordRepository.save(exclusionKeyword);
    }
    @Transactional
    public boolean deleteExclusionKeyword(ExclusionKeywordRequestDto exclusionKeywordRequestDto,String email){
        campaignService.getMyCampaign(exclusionKeywordRequestDto.getCampaignId(),email);
        for(String removeKey : exclusionKeywordRequestDto.getExclusionKeyword()){
            if(exclusionKeywordRepository.deleteByCampaign_campaignIdANDExclusionKeyword(exclusionKeywordRequestDto.getCampaignId(),removeKey)==0){
                throw new ExclusionKeyNotFound();
            }
        }
        return true;
    }

    public List<ExclusionKeywordResponseDto> getExclusionKeywords(Long campaignId){
        List<ExclusionKeyword> exclusionKeywords = exclusionKeywordRepository.findAllByCampaign_campaignId(campaignId);
        if(exclusionKeywords.isEmpty()) throw new ExclusionKeyNotFound();
        return exclusionKeywords.stream().map(TypeChangeExclusionKeyword::entityToResponseDTO).toList();
    }
}
