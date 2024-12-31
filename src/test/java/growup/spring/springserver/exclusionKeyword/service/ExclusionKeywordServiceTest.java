package growup.spring.springserver.exclusionKeyword.service;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.repository.CampaignRepository;
import growup.spring.springserver.exclusionKeyword.domain.ExclusionKeyword;
import growup.spring.springserver.exclusionKeyword.dto.ExclusionKeywordRequestDto;
import growup.spring.springserver.exclusionKeyword.dto.ExclusionKeywordResponseDto;
import growup.spring.springserver.exclusionKeyword.repository.ExclusionKeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        doReturn(Optional.of(getCampaign(1L,"campaign1"))).when(campaignRepository).findByCampaignId(any(Long.class));
        doReturn(true).when(exclusionKeywordRepository).existsByExclusionKeyword("exclusionKeyword1");
        final List<String> keywords = List.of("exclusionKeyword1");
        //given
        final Exception result = assertThrows(IllegalArgumentException.class,
                ()->exclusionKeywordService.addExclusionKeyword(getExclusionRequestDto(keywords,1L))
        );
        //then
        assertThat(result.getMessage()).isEqualTo("이미 해당 제외키워드가 존재합니다.");
    }

    @DisplayName("addExclusionKeyword() : Error2. not found campaign")
    @Test
    void test1_2(){
        //when
        doReturn(Optional.empty()).when(campaignRepository).findByCampaignId(any(Long.class));
        final List<String> keywords = List.of("exclusionKeyword1");
        //given
        final Exception result = assertThrows(IllegalArgumentException.class,
                ()->exclusionKeywordService.addExclusionKeyword(getExclusionRequestDto(keywords,1L))
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
        final List<String> keywords = List.of("exclusionKeyword1");
        //given
        final List<ExclusionKeywordResponseDto> result = exclusionKeywordService.addExclusionKeyword(getExclusionRequestDto(keywords,1L));
        //then
        assertThat(result.get(0).getExclusionKeyword()).isEqualTo("exclusionKeyword1");
        assertThat(result.get(0).getCampaignId()).isEqualTo(1L);
    }

    @DisplayName("deleteExclusionKeyword() : Error1. 해당 제외키워드가 존재하지 않음")
    @Test
    void test2_1(){
        //when
        doReturn(Optional.of(getCampaign(1L,"campaign1"))).when(campaignRepository).findByCampaignId(any(Long.class));
        doReturn(0).when(exclusionKeywordRepository).deleteByCampaign_campaignIdANDExclusionKeyword(any(Long.class),any(String.class));
        final List<String> keywords = List.of("exclusionKeyword1");
        //given
        final Exception result = assertThrows(IllegalArgumentException.class,
                ()->exclusionKeywordService.deleteExclusionKeyword(getExclusionRequestDto(keywords,1L))
        );
        //then
        assertThat(result.getMessage()).isEqualTo("해당 제외키워드가 존재하지 않습니다.");
    }

    @DisplayName("deleteExclusionKeyword() : Error2. 해당 캠패인이 없는 경우")
    @Test
    void test2_2(){
        //when
        doReturn(Optional.empty()).when(campaignRepository).findByCampaignId(any(Long.class));
        final List<String> keywords = List.of("exclusionKeyword1");
        //given
        final Exception result = assertThrows(IllegalArgumentException.class,
                ()->exclusionKeywordService.deleteExclusionKeyword(getExclusionRequestDto(keywords,1L))
        );
        //then
        assertThat(result.getMessage()).isEqualTo("해당 캠패인이 존재하지 않습니다.");
    }


    @DisplayName("deleteExclusionKeyword() : Success. 삭제 성공~")
    @Test
    void test2_3(){
        //when
        doReturn(Optional.of(getCampaign(1L,"campaign1"))).when(campaignRepository).findByCampaignId(any(Long.class));
        doReturn(1).when(exclusionKeywordRepository).deleteByCampaign_campaignIdANDExclusionKeyword(any(Long.class),any(String.class));
        final List<String> keywords = List.of("exclusionKeyword1");
        //given
        final boolean result = exclusionKeywordService.deleteExclusionKeyword(getExclusionRequestDto(keywords,1L));
        //then
        assertThat(result).isEqualTo(true);
    }

    @DisplayName("getExclusionKeywords(): Error1. 데이터가 존재하지 않음")
    @Test
    void test3_1(){
        //when
        doReturn(List.of()).when(exclusionKeywordRepository).findAllByCampaign_campaignId(any(Long.class));
        //given
        final Exception result = assertThrows(IllegalArgumentException.class,
                () -> exclusionKeywordService.getExclusionKeywords(1L));
        //then
        assertThat(result.getMessage()).isEqualTo("해당 제외키워드가 없습니다");
    }
    @DisplayName("getExclusionKeywords(): Success")
    @Test
    void test3_2(){
        //when
        doReturn(List.of(getExclusionKeyword(1L,"exK1",getCampaign(1L,"cam1")),
                getExclusionKeyword(2L,"exK2",getCampaign(1L,"cam1")),
                getExclusionKeyword(3L,"exK3",getCampaign(1L,"cam1")))).when(exclusionKeywordRepository).findAllByCampaign_campaignId(any(Long.class));
        //given
        final List<ExclusionKeywordResponseDto> result = exclusionKeywordService.getExclusionKeywords(1L);
        //then
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getExclusionKeyword()).isEqualTo("exK1");
        assertThat(result.get(1).getExclusionKeyword()).isEqualTo("exK2");
        assertThat(result.get(2).getExclusionKeyword()).isEqualTo("exK3");
        assertThat(result.get(0).getAddTime()).isEqualTo(LocalDate.now());
    }


    public ExclusionKeyword getExclusionKeyword(Long id, String key, Campaign campaign) {
        return ExclusionKeyword.builder()
                .id(id)
                .exclusionKeyword(key)
                .insDate(LocalDateTime.now())
                .campaign(campaign)
                .build();
    }

    public Campaign getCampaign(Long id,String title){
        return Campaign.builder()
                .campaignId(id)
                .camCampaignName(title)
                .build();
    }

    public ExclusionKeywordRequestDto getExclusionRequestDto(List<String> key, Long campaignId){
        return ExclusionKeywordRequestDto.builder()
                .exclusionKeyword(key)
                .campaignId(campaignId)
                .build();
    }
}
