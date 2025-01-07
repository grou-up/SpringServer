package growup.spring.springserver.keyword.service;

import growup.spring.springserver.exception.InvalidDateFormatException;
import growup.spring.springserver.exception.keyword.CampaignKeywordNotFoundException;
import growup.spring.springserver.keyword.domain.Keyword;
import growup.spring.springserver.keyword.repository.KeywordRepository;
import growup.spring.springserver.keyword.dto.KeywordResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class KeywordServiceTest {
    @Mock
    private KeywordRepository keywordRepository;
    @Mock
    private ExclusionKeywordService exclusionKeywordService;

    @InjectMocks
    private KeywordService keywordService;

    @Test
    @DisplayName("getKeyWords(): Error 1. 해당 조건 데이터 없음")
    void test1_1(){
        //when
        doReturn(List.of()).when(keywordRepository).findAllByDateANDCampaign(any(LocalDate.class),any(LocalDate.class),any(Long.class));
        //given
        final Exception result  = assertThrows(CampaignKeywordNotFoundException.class,
                ()-> keywordService.getKeywordsByCampaignId("2024-12-24","2024-12-24",1L) );
        //then
        assertThat(result.getMessage()).isEqualTo("해당 캠페인의 키워드가 없습니다.");
    }
    @Test
    @DisplayName("getKeyWords(): Error 2. 조회 시작날과 끝의 순서가 이상할 때")
    void test1_2(){
        //when
        final String start = "2024-12-14";
        final String end = "2023-12-14";
        //given
        final Exception result  = assertThrows(InvalidDateFormatException.class,
                ()-> keywordService.getKeywordsByCampaignId(start,end,1L) );
        //then
        assertThat(result.getMessage()).isEqualTo("날짜 형식이 이상합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"20-10-11","2024-1-1","2024-01-1","2024-01-10-17"})
    @DisplayName("getKeyWords(): Error 3. LocalDate 형식 오류")
    void test1_3(String wrongDataFormat){
        //when
        final String start = wrongDataFormat;
        final String end = "2023-12-14";
        //given
        final Exception result  = assertThrows(InvalidDateFormatException.class,
                ()-> keywordService.getKeywordsByCampaignId(start,end,1L) );
        //then
        assertThat(result.getMessage()).isEqualTo("날짜 형식이 이상합니다.");
    }

    @Test
    @DisplayName("getKeyWords(): Success")
    void test1_4(){
        //when
        final String start = "2024-12-14";
        final String end = "2025-12-14";
        doReturn(List.of(getKeyword("keyword1",0.0,0.0,0L,0L,0L,0.0)
                ,getKeyword("keyword2",0.0,0.0,0L,0L,0L,0.0)
                ,getKeyword("keyword3",0.0,0.0,0L,0L,0L,0.0)))
                .when(keywordRepository).findAllByDateANDCampaign(any(LocalDate.class),any(LocalDate.class),any(Long.class));
        //given
        final List<KeywordResponseDto> result  = keywordService.getKeywordsByCampaignId(start,end,1L);
        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("getKeyWords(): Error 4. Roas, Cvr 계산 시 분모가 0 일 경우?")
    void test1_5(){
        //when
        final String start = "2024-12-14";
        final String end = "2025-12-14";
        doReturn(List.of(getKeyword("keyword1",0.0,0.0,0L,0L,0L,0.0)
                    ,getKeyword("keyword1",0.0,0.0,0L,0L,0L,0.0)
                    ,getKeyword("keyword1",0.0,0.0,0L,0L,0L,0.0)))
                .when(keywordRepository).findAllByDateANDCampaign(any(LocalDate.class),any(LocalDate.class),any(Long.class));
        //given
        final List<KeywordResponseDto> result  = keywordService.getKeywordsByCampaignId(start,end,1L);
        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getKeyKeyword()).isEqualTo("keyword1");
        assertThat(result.get(0).getKeyCvr()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("getKeyWords(): Success 2. keyClickRate Test")
    void test1_6(){
        //when
        final String start = "2024-12-14";
        final String end = "2025-12-14";
        doReturn(List.of(getKeyword("keyword1",660.0,0.0,10L,123L,0L,11000.0)
                ,getKeyword("keyword1",0.0,0.0,10L,123L,0L,0.0)
                ,getKeyword("keyword1",0.0,0.0,10L,123L,0L,0.0)))
                .when(keywordRepository).findAllByDateANDCampaign(any(LocalDate.class),any(LocalDate.class),any(Long.class));
        //given
        final List<KeywordResponseDto> result  = keywordService.getKeywordsByCampaignId(start,end,1L);
        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getKeyClicks()).isEqualTo(30);
        assertThat(result.get(0).getKeyImpressions()).isEqualTo(369);
        assertThat(result.get(0).getKeyClickRate()).isEqualTo(((Math.round(((double)30/(double) 369)*10000))/100.0));
    }

    @Test
    @DisplayName("getKeyWords(): Success 3. cvr Test")
    void test1_7(){
        //when
        final String start = "2024-12-14";
        final String end = "2025-12-14";
        doReturn(List.of(getKeyword("keyword1",660.0,0.0,10L,123L,2L,11000.0)
                ,getKeyword("keyword1",0.0,0.0,10L,123L,4L,0.0)
                ,getKeyword("keyword1",0.0,0.0,112L,123L,34L,0.0)))
                .when(keywordRepository).findAllByDateANDCampaign(any(LocalDate.class),any(LocalDate.class),any(Long.class));
        //given
        final List<KeywordResponseDto> result  = keywordService.getKeywordsByCampaignId(start,end,1L);
        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getKeyClicks()).isEqualTo(132);
        assertThat(result.get(0).getKeyTotalSales()).isEqualTo(40);
        assertThat(result.get(0).getKeyCvr()).isEqualTo(((Math.round(((double)40/(double) 132)*10000))/100.0));
    }

    @Test
    @DisplayName("getKeyWords(): Success 4. cpc Test")
    void test1_8(){
        //when
        final String start = "2024-12-14";
        final String end = "2025-12-14";
        doReturn(List.of(getKeyword("keyword1",660.0,0.0,10L,123L,2L,11000.0)
                ,getKeyword("keyword1",660.0,0.0,10L,123L,4L,0.0)
                ,getKeyword("keyword1",660.0,0.0,112L,123L,34L,0.0)))
                .when(keywordRepository).findAllByDateANDCampaign(any(LocalDate.class),any(LocalDate.class),any(Long.class));
        //given
        final List<KeywordResponseDto> result  = keywordService.getKeywordsByCampaignId(start,end,1L);
        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getKeyClicks()).isEqualTo(132);
        assertThat(result.get(0).getKeyAdcost()).isEqualTo(1980);
        assertThat(result.get(0).getKeyCpc()).isEqualTo(((Math.round(((double)1980/(double) 132)*100))/100.0));
    }

    @Test
    @DisplayName("getKeyWords(): Success 5. 제외 키워드 처리")
    void test1_9(){
        //when
        final String start = "2024-12-14";
        final String end = "2025-12-14";
        doReturn(List.of(getKeyword("exKey1",660.0,0.0,10L,123L,2L,11000.0)
                ,getKeyword("exKey2",660.0,0.0,10L,123L,4L,0.0)
                ,getKeyword("exKey3",660.0,0.0,112L,123L,34L,0.0)))
                .when(keywordRepository).findAllByDateANDCampaign(any(LocalDate.class),any(LocalDate.class),any(Long.class));
        doReturn(List.of(
                getExclusionKeyword("exKey1",1L,LocalDate.now()),
                getExclusionKeyword("exKey10",1L,LocalDate.now()),
                getExclusionKeyword("exKey100",1L,LocalDate.now()))
        ).when(exclusionKeywordService).getExclusionKeywords(1L);
        //given
        final List<KeywordResponseDto> result  = keywordService.getKeywordsByCampaignId(start,end,1L);
        //then
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getKeyExcludeFlag()).isTrue();
        assertThat(result.get(1).getKeyExcludeFlag()).isFalse();
        assertThat(result.get(2).getKeyExcludeFlag()).isFalse();
    }

    @DisplayName("getExclusionKeywordsToSet(): Error1. 제외키워드가 없을 경우")
    @Test
    void test2_1(){
        //when
        doThrow(IllegalArgumentException.class)
                .when(exclusionKeywordService).getExclusionKeywords(1L);
        //given
        Set<String> result = keywordService.getExclusionKeywordToSet(1L);
        //then
        assertThat(result).hasSize(0);
    }

    @DisplayName("getExclusionKeywordsToSet(): Success")
    @Test
    void test2_2(){
        //when
        doReturn(List.of(
                getExclusionKeyword("exKey1",1L,LocalDate.now()),
                getExclusionKeyword("exKey2",1L,LocalDate.now()),
                getExclusionKeyword("exKey3",1L,LocalDate.now()))
        ).when(exclusionKeywordService).getExclusionKeywords(1L);
        //given
        Set<String> result = keywordService.getExclusionKeywordToSet(1L);
        //then
        assertThat(result).hasSize(3);
        assertThat(result.contains("exKey1")).isTrue();
        assertThat(result.contains("exKey4")).isFalse();
    }

    public ExclusionKeywordResponseDto getExclusionKeyword(String key,
                                                           Long campaignId,
                                                           LocalDate localDate){
        return ExclusionKeywordResponseDto.builder()
                .exclusionKeyword(key)
                .campaignId(campaignId)
                .addTime(localDate)
                .build();
    }

    public Keyword getKeyword(String title,
                              Double adCost,
                              Double cpc,
                              Long clicks,
                              Long impression,
                              Long totalSale,
                              Double adSale){
        return Keyword.builder()
                .keyCvr(0.0)
                .keyClickRate(0.0)
                .keyRoas(0.0)
                .keyKeyword(title)
                .keyAdcost(adCost)
                .keyCpc(cpc)
                .keyClicks(clicks)
                .keyImpressions(impression)
                .keyTotalSales(totalSale)
                .keyAdsales(adSale)
                .build();
    }


}
