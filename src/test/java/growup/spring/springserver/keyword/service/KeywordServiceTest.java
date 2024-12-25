package growup.spring.springserver.keyword.service;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class KeywordServiceTest {
    @Mock
    private KeywordRepository keywordRepository;

    @InjectMocks
    private KeywordService keywordService;

    @Test
    @DisplayName("getKeyWords(): Error 1. 해당 조건 데이터 없음")
    void test1(){
        //when
        doReturn(List.of()).when(keywordRepository).findAllByDateANDFlag(any(LocalDate.class),any(LocalDate.class),any(Long.class));
        //given
        final Exception result  = assertThrows(NullPointerException.class,
                ()-> keywordService.getKeywordsByCampaignId("2024-12-24","2024-12-24",1L) );
        //then
        assertThat(result.getMessage()).isEqualTo("해당 캠페인의 키워드가 없습니다.");
    }
    @Test
    @DisplayName("getKeyWords(): Error 2. 조회 시작날과 끝의 순서가 이상할 때")
    void test2(){
        //when
        final String start = "2024-12-14";
        final String end = "2023-12-14";
        //given
        final Exception result  = assertThrows(IllegalArgumentException.class,
                ()-> keywordService.getKeywordsByCampaignId(start,end,1L) );
        //then
        assertThat(result.getMessage()).isEqualTo("날짜 형식이 이상합니다");
    }

    @ParameterizedTest
    @ValueSource(strings = {"20-10-11","2024-1-1","2024-01-1","2024-01-10-17"})
    @DisplayName("getKeyWords(): Error 3. LocalDate 형식 오류")
    void test3(String wrongDataFormat){
        //when
        final String start = wrongDataFormat;
        final String end = "2023-12-14";
        //given
        final Exception result  = assertThrows(IllegalArgumentException.class,
                ()-> keywordService.getKeywordsByCampaignId(start,end,1L) );
        //then
        assertThat(result.getMessage()).isEqualTo("날짜 형식이 이상합니다");
    }

    @Test
    @DisplayName("getKeyWords(): Success")
    void test4(){
        //when
        final String start = "2024-12-14";
        final String end = "2025-12-14";
        doReturn(List.of(getKeyword("keyword1")
                ,getKeyword("keyword2")
                ,getKeyword("keyword3")))
                .when(keywordRepository).findAllByDateANDFlag(any(LocalDate.class),any(LocalDate.class),any(Long.class));
        //given
        final List<KeywordResponseDto> result  = keywordService.getKeywordsByCampaignId(start,end,1L);
        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("getKeyWords(): Error 4. Roas, Cvr 계산 시 분모가 0 일 경우?")
    void test6(){
        //when
        final String start = "2024-12-14";
        final String end = "2025-12-14";
        doReturn(List.of(Keyword.builder()
                                .keyKeyword("keyword1")
                                .keyAdcost(0.0)
                                .keyCvr(0.0)
                                .keyCpc(0.0)
                                .keyClicks(0L)
                                .keyImpressions(0L)
                                .keyTotalSales(0L)
                                .keyAdsales(0.0)
                                .build()
                    ,Keyword.builder()
                        .keyKeyword("keyword1")
                        .keyAdcost(0.0)
                        .keyCpc(0.0)
                        .keyCvr(0.0)
                        .keyClicks(0L)
                        .keyImpressions(0L)
                        .keyTotalSales(0L)
                        .keyAdsales(0.0)
                        .build()
                ,Keyword.builder()
                        .keyKeyword("keyword1")
                        .keyAdcost(0.0)
                        .keyCpc(0.0)
                        .keyCvr(0.0)
                        .keyClicks(0L)
                        .keyImpressions(0L)
                        .keyTotalSales(0L)
                        .keyAdsales(0.0)
                        .build()))
                .when(keywordRepository).findAllByDateANDFlag(any(LocalDate.class),any(LocalDate.class),any(Long.class));
        //given
        final List<KeywordResponseDto> result  = keywordService.getKeywordsByCampaignId(start,end,1L);
        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getKeyKeyword()).isEqualTo("keyword1");
        assertThat(result.get(0).getKeyCvr()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("getKeyWords(): Success 2. keyword 요소가 잘 합쳐질까?")
    void test5(){
        //when
        final String start = "2024-12-14";
        final String end = "2025-12-14";
        doReturn(List.of(getKeyword("keyword1")
                ,getKeyword("keyword1")
                ,getKeyword("keyword1")
                ,getKeyword("keyword2")))
                .when(keywordRepository).findAllByDateANDFlag(any(LocalDate.class),any(LocalDate.class),any(Long.class));
        //given
        final List<KeywordResponseDto> result  = keywordService.getKeywordsByCampaignId(start,end,1L);
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getKeyKeyword()).isEqualTo("keyword1");
        assertThat(result.get(0).getKeyAdcost()).isEqualTo(150.0);
        assertThat(result.get(0).getKeyClickRate()).isEqualTo(100.0);
        assertThat(result.get(0).getKeyRoas()).isEqualTo(200.0);
        assertThat(result.get(0).getKeyCvr()).isEqualTo(100.0);
    }

    public Keyword getKeyword(String title){
        return Keyword.builder()
                .keyKeyword(title)
                .keyAdcost(50.0)
                .keyCpc(100.0)
                .keyClicks(100L)
                .keyImpressions(100L)
                .keyTotalSales(100L)
                .keyAdsales(100.0)
                .build();
    }


}
