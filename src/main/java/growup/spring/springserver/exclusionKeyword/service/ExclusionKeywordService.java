package growup.spring.springserver.exclusionKeyword.service;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.repository.CampaignRepository;
import growup.spring.springserver.exclusionKeyword.TypeChangeExclusionKeyword;
import growup.spring.springserver.exclusionKeyword.domain.ExclusionKeyword;
import growup.spring.springserver.exclusionKeyword.dto.ExclusionKeywordResponseDto;
import growup.spring.springserver.exclusionKeyword.repository.ExclusionKeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExclusionKeywordService {
    @Autowired
    private ExclusionKeywordRepository exclusionKeywordRepository;
    @Autowired
    private CampaignRepository campaignRepository;

    public ExclusionKeywordResponseDto addExclusionKeyword(Long campaignId, String keyword){
        if(exclusionKeywordRepository.existsByExclusionKeyword(keyword)) throw new IllegalArgumentException("이미 해당 제외키워드가 존재합니다.");
        Campaign campaign = campaignRepository.findByCampaignId(campaignId).orElseThrow(
                ()-> new IllegalArgumentException("해당 캠패인이 존재하지 않습니다.")
        );
        ExclusionKeyword exclusionKeyword = ExclusionKeyword.builder()
                .exclusionKeyword(keyword)
                .campaign(campaign)
                .build();
        ExclusionKeyword result = exclusionKeywordRepository.save(exclusionKeyword);
        return TypeChangeExclusionKeyword.entityToResponseDTO(result);
    }

    public boolean deleteExclusionKeyword(Long campaignId, String exclusionKeyword){
        if(exclusionKeyword.isBlank()) throw new IllegalArgumentException("제거할 키워드가 입력되지 않았습니다.");
        if(exclusionKeywordRepository.deleteByCampaign_campaignIdANDExclusionKeyword(campaignId,exclusionKeyword)==0){
            throw new IllegalArgumentException("해당 제외키워드가 존재하지 않습니다.");
        }
        return true;
    }

    public List<String> getExclusionKeywords(Long campaignId){
        List<ExclusionKeyword> exclusionKeywords = exclusionKeywordRepository.findAllByCampaign_campaignId(campaignId);
        if(exclusionKeywords.isEmpty()) throw new IllegalArgumentException("해당 제외키워드가 없습니다");
        return exclusionKeywords.stream().map(ExclusionKeyword::getExclusionKeyword).toList();
    }
}