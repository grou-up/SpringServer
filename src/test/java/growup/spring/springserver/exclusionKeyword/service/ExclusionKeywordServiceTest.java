package growup.spring.springserver.exclusionKeyword.service;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.repository.CampaignRepository;
import growup.spring.springserver.exclusionKeyword.domain.ExclusionKeyword;
import growup.spring.springserver.exclusionKeyword.dto.ExclusionKeywordResponseDto;
import growup.spring.springserver.exclusionKeyword.repository.ExclusionKeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ExclusionKeywordServiceTest {
    @InjectMocks
    private ExclusionKeywordService exclusionKeywordService;

    @Mock
    private ExclusionKeywordRepository exclusionKeywordRepository;
    @Mock
    private CampaignRepository campaignRepository;

    @DisplayName("addExclusionKeyword() : Error1. already exist Same Keyword")
    @Test
    void test1_1(){
        //when
        doReturn(true).when(exclusionKeywordRepository).existsByExclusionKeyword("exclusionKeyword1");
        //given
        final Exception result = assertThrows(IllegalArgumentException.class,
                ()->exclusionKeywordService.addExclusionKeyword(1L,"exclusionKeyword1")
        );
        //then
        assertThat(result.getMessage()).isEqualTo("이미 해당 제외키워드가 존재합니다.");
    }

    @DisplayName("addExclusionKeyword() : Error2. not found campaign")
    @Test
    void test1_2(){
        //when
        doReturn(Optional.empty()).when(campaignRepository).findByCampaignId(any(Long.class));
        //given
        final Exception result = assertThrows(IllegalArgumentException.class,
                ()->exclusionKeywordService.addExclusionKeyword(1L,"exclusionKeyword1")
        );
        //then
        assertThat(result.getMessage()).isEqualTo("해당 캠패인이 존재하지 않습니다.");
    }

    @DisplayName("addExclusionKeyword() : Success")
    @Test
    void test1_3(){
        //when
        doReturn(Optional.of(getCampaign(1L,"campaign1"))).when(campaignRepository).findByCampaignId(any(Long.class));
        doReturn(false).when(exclusionKeywordRepository).existsByExclusionKeyword("exclusionKeyword1");
        doReturn(getExclusionKeyword(1L,"exclusionKeyword1",getCampaign(1L,"campaign1")))
                .when(exclusionKeywordRepository)
                .save(any(ExclusionKeyword.class));
        //given
        final ExclusionKeywordResponseDto result = exclusionKeywordService.addExclusionKeyword(1L,"exclusionKeyword1");
        //then
        assertThat(result.getExclusionKeyword()).isEqualTo("exclusionKeyword1");
        assertThat(result.getCampaignId()).isEqualTo(1L);
    }

    @DisplayName("deleteExclusionKeyword() : Error1. 해당 제외키워드가 존재하지 않음")
    @Test
    void test2_1(){
        //when
        doReturn(0).when(exclusionKeywordRepository).deleteByCampaign_campaignIdANDExclusionKeyword(any(Long.class),any(String.class));
        //given
        final Exception result = assertThrows(IllegalArgumentException.class,
                ()->exclusionKeywordService.deleteExclusionKeyword(1L,"exclusionKeyword1")
        );
        //then
        assertThat(result.getMessage()).isEqualTo("해당 제외키워드가 존재하지 않습니다.");
    }

    @DisplayName("deleteExclusionKeyword() : Error2. 파라미터가 빈 값인 경우")
    @Test
    void test2_2(){
        //when
        String emptyString ="";
        //given
        final Exception result = assertThrows(IllegalArgumentException.class,
                ()->exclusionKeywordService.deleteExclusionKeyword(1L,emptyString)
        );
        //then
        assertThat(result.getMessage()).isEqualTo("제거할 키워드가 입력되지 않았습니다.");
    }


    @DisplayName("deleteExclusionKeyword() : Success. 삭제 성공~")
    @Test
    void test2_3(){
        //when
        doReturn(1).when(exclusionKeywordRepository).deleteByCampaign_campaignIdANDExclusionKeyword(any(Long.class),any(String.class));
        //given
        final boolean result = exclusionKeywordService.deleteExclusionKeyword(1L,"exclusionKey");
        //then
        assertThat(result).isEqualTo(true);
    }


    public ExclusionKeyword getExclusionKeyword(Long id, String key, Campaign campaign){
        return ExclusionKeyword.builder()
                .id(id)
                .exclusionKeyword(key)
                .campaign(campaign)
                .build();
    }

    public Campaign getCampaign(Long id,String title){
        return Campaign.builder()
                .campaignId(id)
                .camCampaignName(title)
                .build();
    }
}
