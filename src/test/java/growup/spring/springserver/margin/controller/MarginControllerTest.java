package growup.spring.springserver.margin.controller;

import com.nimbusds.jose.shaded.gson.Gson;
import growup.spring.springserver.annotation.WithAuthUser;
import growup.spring.springserver.global.config.JwtTokenProvider;
import growup.spring.springserver.margin.dto.DailyAdSummaryDto;
import growup.spring.springserver.margin.dto.MarginSummaryResponseDto;
import growup.spring.springserver.margin.service.MarginService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MarginController.class)
class MarginControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MarginService marginService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    @DisplayName("getCampaignAllSales() : error 1. 데이터 누락")
    @WithAuthUser
    void test1() throws Exception {
        final String badUrl = "/api/margin/getCampaignAllSales?date=";
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(badUrl));
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("getCampaignAllSales() : 성공 케이스")
    @WithAuthUser
    void testGetCampaignAllSalesSuccess() throws Exception {
        Gson gson = new Gson();
        // Given
        final String url = "/api/margin/getCampaignAllSales?date=2024-12-01";
        LocalDate targetDate = LocalDate.of(2024, 11, 11);
        List<MarginSummaryResponseDto> mockResponse = List.of(
                MarginSummaryResponseDto.builder()
                        .date(targetDate)
                        .campaignId(1L)
                        .campaignName("Campaign 1")
                        .todaySales(300.0)
                        .yesterdaySales(200.0)
                        .differentSales(100.0)
                        .build(),
                MarginSummaryResponseDto.builder()
                        .date(targetDate)
                        .campaignId(2L)
                        .campaignName("Campaign 2")
                        .todaySales(250.0)
                        .yesterdaySales(150.0)
                        .differentSales(100.0)
                        .build()
        );
        doReturn(mockResponse).when(marginService).getCampaignAllSales(anyString(), any(LocalDate.class));
        System.out.println("mockResponse = " + mockResponse);
        // When
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url));

        // then
        resultActions.andDo(print());

        resultActions.andExpectAll(
                status().isOk(),
                jsonPath("$.message").value("success : getMyCampaignDetails"),
                jsonPath("$.data").isArray(),
                jsonPath("$.data[0].date").value("2024-11-11"),
                jsonPath("$.data[0].campaignId").value(1),
                jsonPath("$.data[0].campaignName").value("Campaign 1"),
                jsonPath("$.data[0].yesterdaySales").value(200.0),
                jsonPath("$.data[0].todaySales").value(300.0),
                jsonPath("$.data[0].differentSales").value(100.0),
                jsonPath("$.data[1].campaignId").value(2),
                jsonPath("$.data[1].campaignName").value("Campaign 2"),
                jsonPath("$.data[1].todaySales").value(250.0)
        );
    }

    @Test
    @WithAuthUser
    @DisplayName("getDailyAdSummary() : 성공 케이스")
    void getDailyAdSummary() throws Exception {
        // Given
        LocalDate date = LocalDate.of(2024, 12, 1);
        List<DailyAdSummaryDto> mockResponse = List.of(
                new DailyAdSummaryDto(date.minusDays(1), 200.0, 400.0, 200.0),
                new DailyAdSummaryDto(date, 300.0, 600.0, 200.0)
        );

        // Mocking 서비스 호출
        doReturn(mockResponse).when(marginService).findByCampaignIdsAndDates(any(String.class), any(LocalDate.class));

        // API 호출 URL
        final String url = "/api/margin/getDailyAdSummary?date=2024-12-01";

        // When & Then
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url));
        resultActions.andDo(print());
        resultActions
                .andExpect(status().isOk()) // HTTP 상태 코드 200
                .andExpect(jsonPath("$.message").value("success: getDailyAdSummary")) // 응답 메시지 검증
                .andExpect(jsonPath("$.data").isArray()) // 데이터가 배열인지 확인
                .andExpect(jsonPath("$.data[0].marDate").value("2024-11-30"))
                .andExpect(jsonPath("$.data[0].marAdCost").value(200.0))
                .andExpect(jsonPath("$.data[0].marSales").value(400.0))
                .andExpect(jsonPath("$.data[0].marRoas").value(200.0))
                .andExpect(jsonPath("$.data[1].marDate").value("2024-12-01"))
                .andExpect(jsonPath("$.data[1].marAdCost").value(300.0))
                .andExpect(jsonPath("$.data[1].marSales").value(600.0))
                .andExpect(jsonPath("$.data[1].marRoas").value(200.0));
        // Verify 서비스 호출
        verify(marginService).findByCampaignIdsAndDates(any(String.class), any(LocalDate.class));
    }

}