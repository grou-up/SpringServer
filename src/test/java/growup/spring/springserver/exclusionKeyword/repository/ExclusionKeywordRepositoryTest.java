package growup.spring.springserver.exclusionKeyword.repository;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.repository.CampaignRepository;
import growup.spring.springserver.exclusionKeyword.domain.ExclusionKeyword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ExclusionKeywordRepositoryTest {
    @Autowired
    private ExclusionKeywordRepository exclusionKeywordRepository;
    @Autowired
    private CampaignRepository campaignRepository;

    @DisplayName("save() : Success")
    @Test
    void test1(){
        //when
        final ExclusionKeyword exclusionKeyword = getExclusionKeyword("exclusionKey",null);
        //given
        ExclusionKeyword result = exclusionKeywordRepository.save(exclusionKeyword);
        //then
        assertThat(result.getExclusionKeyword()).isEqualTo("exclusionKey");
    }
    @DisplayName("findAllByCampaignId() : Success")
    @Test
    void test2(){
        //when
        final Campaign campaign = campaignRepository.save(getCampaign("campaign"));
        exclusionKeywordRepository.save(getExclusionKeyword("exclusionKey1",campaign));
        exclusionKeywordRepository.save(getExclusionKeyword("exclusionKey2",campaign));
        exclusionKeywordRepository.save(getExclusionKeyword("exclusionKey3",campaign));
        //given
        final List<ExclusionKeyword> result = exclusionKeywordRepository.findAllByCampaign_campaignId(1L);
        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @DisplayName("deleteById() : Success")
    @Test
    void test3(){
        //when
        final Campaign campaign = campaignRepository.save(getCampaign("campaign"));
        ExclusionKeyword exclusionKeyword = exclusionKeywordRepository.save(getExclusionKeyword("exclusionKey1",campaign));
        exclusionKeywordRepository.save(getExclusionKeyword("exclusionKey2",campaign));
        exclusionKeywordRepository.save(getExclusionKeyword("exclusionKey3",campaign));
        //given
        exclusionKeywordRepository.deleteById(exclusionKeyword.getId());
        final List<ExclusionKeyword> result = exclusionKeywordRepository.findAllByCampaign_campaignId(1L);
        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @DisplayName("existsByExclusionKeyword() : Success")
    @Test
    void test4(){
        //when
        final Campaign campaign = campaignRepository.save(getCampaign("campaign"));
        exclusionKeywordRepository.save(getExclusionKeyword("exclusionKey1",campaign));
        exclusionKeywordRepository.save(getExclusionKeyword("exclusionKey2",campaign));
        exclusionKeywordRepository.save(getExclusionKeyword("exclusionKey3",campaign));
        //given
        final boolean result = exclusionKeywordRepository.existsByExclusionKeyword("exclusionKey1");
        //then
        assertThat(result).isEqualTo(true);
    }

    public ExclusionKeyword getExclusionKeyword(String exclusionKeyword, Campaign campaign){
        return ExclusionKeyword.builder()
                .campaign(campaign)
                .exclusionKeyword(exclusionKeyword)
                .build();
    }
    public Campaign getCampaign(String name){
        return Campaign.builder()
                .campaignId(1L)
                .camCampaignName(name)
                .build();
    }


}
