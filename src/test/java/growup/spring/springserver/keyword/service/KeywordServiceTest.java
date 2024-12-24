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

    public Keyword getKeyword(String title){
        return Keyword.builder()
                .keyKeyword(title)
                .build();
    }


}
