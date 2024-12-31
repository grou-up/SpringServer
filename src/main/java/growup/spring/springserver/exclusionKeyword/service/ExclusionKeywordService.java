package growup.spring.springserver.exclusionKeyword.service;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.repository.CampaignRepository;
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

    public List<ExclusionKeywordResponseDto> addExclusionKeyword(ExclusionKeywordRequestDto exclusionKeywordRequestDto){
        Campaign campaign = campaignRepository.findByCampaignId(exclusionKeywordRequestDto.getCampaignId()).orElseThrow(
                ()-> new IllegalArgumentException("해당 캠패인이 존재하지 않습니다.")
        );
        List<ExclusionKeywordResponseDto> exclusionKeywordResponseDtos = new ArrayList<>();
        for(String keyword : exclusionKeywordRequestDto.getExclusionKeyword()){
            if(exclusionKeywordRepository.existsByExclusionKeyword(keyword))
                throw new IllegalArgumentException("이미 해당 제외키워드가 존재합니다.");
            ExclusionKeyword exclusionKeyword = ExclusionKeyword.builder()
                    .exclusionKeyword(keyword)
                    .campaign(campaign)
                    .build();
            ExclusionKeyword result = exclusionKeywordRepository.save(exclusionKeyword);
            exclusionKeywordResponseDtos.add(TypeChangeExclusionKeyword.entityToResponseDTO(result));
        }
        return exclusionKeywordResponseDtos;
    }

    @Transactional
    public boolean deleteExclusionKeyword(ExclusionKeywordRequestDto exclusionKeywordRequestDto){
        campaignRepository.findByCampaignId(exclusionKeywordRequestDto.getCampaignId()).orElseThrow(
                ()-> new IllegalArgumentException("해당 캠패인이 존재하지 않습니다.")
        );
        for(String removeKey : exclusionKeywordRequestDto.getExclusionKeyword()){
            if(exclusionKeywordRepository.deleteByCampaign_campaignIdANDExclusionKeyword(exclusionKeywordRequestDto.getCampaignId(),removeKey)==0){
                throw new IllegalArgumentException("해당 제외키워드가 존재하지 않습니다.");
            }
        }
        return true;
    }

    public List<ExclusionKeywordResponseDto> getExclusionKeywords(Long campaignId){
        List<ExclusionKeyword> exclusionKeywords = exclusionKeywordRepository.findAllByCampaign_campaignId(campaignId);
        if(exclusionKeywords.isEmpty()) throw new IllegalArgumentException("해당 제외키워드가 없습니다");
        return exclusionKeywords.stream().map(TypeChangeExclusionKeyword::entityToResponseDTO).toList();
    }
}
