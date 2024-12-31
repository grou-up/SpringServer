package growup.spring.springserver.campaignoptiondetails.controller;

import com.nimbusds.jose.shaded.gson.Gson;
import growup.spring.springserver.annotation.WithAuthUser;
import growup.spring.springserver.campaignoptiondetails.dto.CampaignOptionDetailsResponseDto;
import growup.spring.springserver.campaignoptiondetails.service.CampaignOptionDetailsService;
import growup.spring.springserver.global.config.JwtTokenProvider;
import growup.spring.springserver.keyword.controller.KeywordController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CampaignOptionDetailsController.class)
class CampaignOptionDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignOptionDetailsService campaignOptionDetailsService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    private Gson gson;

    @ParameterizedTest
    @DisplayName("getCampaignDetails() : 파라미터 값 누락")
    @WithAuthUser
    @MethodSource("invalidGetCampaignDetailsUrl")
    void test(String url) throws Exception {

        ResultActions resultActions = mockMvc.perform(get(url)
                .with(csrf()));

        resultActions.andExpectAll(
                status().isBadRequest()
        ).andDo(print());
    }

    static Stream<String> invalidGetCampaignDetailsUrl() {
        return Stream.of(
                "/api/cod/getMyCampaignDetails",
                "/api/cod/getMyCampaignDetails?start=2024-01-01",
                "/api/cod/getMyCampaignDetails?campaignId=1",
                "/api/cod/getMyCampaignDetails?start=2024-11-01&end=2024-12-31&campaignId=송보석"
        );
    }

    @DisplayName("getCampaignDetails() : Success")
    @Test
    @WithAuthUser
    void test2() throws Exception {
        final String url = "/api/cod/getMyCampaignDetails?start=2024-01-01&end=2024-12-31&campaignId=1";

        doReturn(List.of(getCampaignOptionDetailsResponseDto()))
                .when(campaignOptionDetailsService).getCampaignDetailsByCampaignsIds(any(LocalDate.class), any(LocalDate.class), any(Long.class));

        //given
        ResultActions resultActions = mockMvc.perform(get(url));

        //then
        resultActions.andDo(print());

        resultActions.andExpectAll(
                status().isOk(),
                jsonPath("data").isArray(),
                jsonPath("data[0].name").value("송보석"),
                jsonPath("data[0].copSales").value(10L),
                jsonPath("data[0].copImpressions").value(100L)
        );
    }

    public CampaignOptionDetailsResponseDto getCampaignOptionDetailsResponseDto() {
        return CampaignOptionDetailsResponseDto.builder()
                .id(1L)
                .name("송보석")
                .copSales(10L)
                .copImpressions(100L)
                .copAdcost(50.0)
                .copAdsales(200.0)
                .copRoas(400.0)
                .copClicks(20L)
                .copClickRate(20.0)
                .copCvr(10.0)
                .build();
    }
}