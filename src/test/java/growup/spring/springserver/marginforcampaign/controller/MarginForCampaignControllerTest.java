package growup.spring.springserver.marginforcampaign.controller;

import com.nimbusds.jose.shaded.gson.Gson;
import growup.spring.springserver.annotation.WithAuthUser;
import growup.spring.springserver.global.config.JwtTokenProvider;
import growup.spring.springserver.marginforcampaign.dto.MarginForCampaignResDto;
import growup.spring.springserver.marginforcampaign.service.MarginForCampaignService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MarginForCampaignController.class)
class MarginForCampaignControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Gson gson;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @MockBean
    private MarginForCampaignService marginForCampaignService;


    @WithAuthUser
    @Test
    @DisplayName("getExecutionAboutCampaign() : ErrorCase 1. 캠페인 ID 누락")
    void getExecutionAboutCampaign_Fail() throws Exception {

        final String url = "/api/marginforcam/getExecutionAboutCampaign?";

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(url));
        result.andExpect(status().isBadRequest());
    }
    @WithAuthUser
    @Test
    @DisplayName("getExecutionAboutCampaign() : SuccessCase 1. 정상 호출")
    void getExecutionAboutCampaign_Succuss() throws Exception {
        final String url = "/api/marginforcam/getExecutionAboutCampaign?campaignId=1";
        gson = new Gson();

        List<MarginForCampaignResDto> mockData = List.of(
                createMarginForCampaignResDto("상품1",1L),
                createMarginForCampaignResDto("상품2",3L)
        );

        // when
        when(marginForCampaignService.marginForCampaignByCampaignId(any(Long.class))).thenReturn(mockData);

        // then
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .content(gson.toJson(mockData))
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].mfcProductName").value("상품1"))
                .andExpect(jsonPath("$.data[0].mfcTotalPrice").value(1L))
                .andExpect(jsonPath("$.data[1].mfcProductName").value("상품2"))
                .andExpect(jsonPath("$.data[1].mfcTotalPrice").value(3L));

    }
    private MarginForCampaignResDto createMarginForCampaignResDto(String productName, Long totalPrice) {
        return MarginForCampaignResDto.builder()
                .mfcProductName(productName)
                .mfcTotalPrice(totalPrice)
                .mfcCostPrice(7000L)
                .mfcPerPiece(3.0)
                .mfcZeroRoas(1.2)
                .build();
    }
}