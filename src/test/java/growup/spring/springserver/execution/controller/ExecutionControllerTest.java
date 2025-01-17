package growup.spring.springserver.execution.controller;

import com.nimbusds.jose.shaded.gson.Gson;
import growup.spring.springserver.annotation.WithAuthUser;
import growup.spring.springserver.execution.dto.ExecutionMarginResDto;
import growup.spring.springserver.execution.service.ExecutionService;
import growup.spring.springserver.global.config.JwtTokenProvider;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ExecutionController.class)
class ExecutionControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;
    @MockBean
    private ExecutionService executionService;


    @WithAuthUser
    @Test
    @DisplayName("getMyExecutionData() : ErrorCase 1. 캠페인 ID 누락")
    void getMyExecutionData_Fail() throws Exception {

        final String url = "/api/execution/getMyExecutionData?";

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(url));
        result.andExpect(status().isBadRequest());
    }
    @WithAuthUser
    @Test
    @DisplayName("getMyExecutionData() : SuccessCase 1. 정상 호출")
    void getMyExecutionData_Success() throws Exception {
        final String url = "/api/execution/getMyExecutionData?campaignId=1";
        Gson gson = new Gson();
        // Mock data
        List<ExecutionMarginResDto> mockData = List.of(
                ExecutionMarginResDto.builder()
                        .exeId(1L)
                        .exeDetailCategory("빨강색")
                        .exeSalePrice(10000L)
                        .exeTotalPrice(15000L)
                        .exeCostPrice(5000L)
                        .build(),
                ExecutionMarginResDto.builder()
                        .exeId(2L)
                        .exeDetailCategory("파랑색")
                        .exeSalePrice(20000L)
                        .exeTotalPrice(25000L)
                        .exeCostPrice(10000L)
                        .build()
        );

        // Mock behavior
        when(executionService.getMyExecutionData(any(Long.class))).thenReturn(mockData);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .content(gson.toJson(mockData))
                .accept(MediaType.APPLICATION_JSON));

        // Expect 200 OK
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].exeId").value(1L))
                .andExpect(jsonPath("$.data[0].exeDetailCategory").value("빨강색"))
                .andExpect(jsonPath("$.data[0].exeSalePrice").value(10000L))
                .andExpect(jsonPath("$.data[0].exeTotalPrice").value(15000L))
                .andExpect(jsonPath("$.data[0].exeCostPrice").value(5000L))
                .andExpect(jsonPath("$.data[1].exeId").value(2L))
                .andExpect(jsonPath("$.data[1].exeDetailCategory").value("파랑색"))
                .andExpect(jsonPath("$.data[1].exeSalePrice").value(20000L))
                .andExpect(jsonPath("$.data[1].exeTotalPrice").value(25000L))
                .andExpect(jsonPath("$.data[1].exeCostPrice").value(10000L));
    }
}