package growup.spring.springserver.keyword.controller;

import growup.spring.springserver.annotation.WithAuthUser;
import growup.spring.springserver.global.config.JwtTokenProvider;
import growup.spring.springserver.keyword.dto.KeywordResponseDto;
import growup.spring.springserver.keyword.service.KeywordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(KeywordController.class)
public class KeywordControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KeywordService keywordService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    @DisplayName("getKeywordsAboutCampaign: Error 1.요청 데이터 누락")
    @WithAuthUser
    void test1() throws Exception {
        final String url = "/api/keyword/getKeywordsAboutCampaign?start=2024-12-01&campaignId=1";
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url));
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("getKeywordsAboutCampaign: Error 2.LocalDate 형식이 이상할 떄")
    @WithAuthUser
    void test1_2() throws Exception {
        final String url = "/api/keyword/getKeywordsAboutCampaign?start=2024-12-01&end=20-11-01&campaignId=1";
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url));
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("getKeywordsAboutCampaign: Success 1.정상 요청")
    @WithAuthUser
    void test2() throws Exception {
        //when
        final String url = "/api/keyword/getKeywordsAboutCampaign?start=2024-12-01&end=2024-12-31&campaignId=1";
        final LocalDate start = LocalDate.of(2024,12,1);
        final LocalDate end = LocalDate.of(2024,12,31);
        doReturn(List.of(
                getKeywordResDto(),
                getKeywordResDto(),
                getKeywordResDto())).when(keywordService).getKeywordsByCampaignId(start,end,1L);
        //given
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url));
        //then
        resultActions.andDo(print());
        resultActions.andExpectAll(
                status().isOk(),
                jsonPath("data").isArray(),
                jsonPath("data[0].keyKeyword").value("keyword"),
                jsonPath("data[1].keyCpc").value(1.0),
                jsonPath("data[2].keyClicks").value(10L)
        );
    }

    public KeywordResponseDto getKeywordResDto(){
        return KeywordResponseDto.builder()
                .keyKeyword("keyword")
                .keyAdcost(1.0)
                .keyClickRate(0.8)
                .keyClicks(10L)
                .keyAdsales(1.0)
                .keyCpc(1.0)
                .keyCvr(1.0)
                .keyImpressions(1L)
                .keyRoas(1.0)
                .keySearchType("test")
                .keyTotalSales(1L)
                .build();
    }
}
