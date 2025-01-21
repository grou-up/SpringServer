package growup.spring.springserver.execution.controller;

import com.nimbusds.jose.shaded.gson.Gson;
import growup.spring.springserver.annotation.WithAuthUser;
import growup.spring.springserver.execution.domain.Execution;
import growup.spring.springserver.execution.dto.ExecutionRequestDto;
import growup.spring.springserver.execution.dto.ExecutionResponseDto;
import growup.spring.springserver.execution.service.ExecutionService;
import growup.spring.springserver.global.config.JwtTokenProvider;
import growup.spring.springserver.login.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExecutionController.class)
public class ExecutionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private Gson gson;

    @MockBean
    private ExecutionService executionService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @DisplayName("/api/execution/getByCampaignIdAndExeIds : Param 값 누락 시")
    @ParameterizedTest
    @MethodSource("nullBodies")
    @WithAuthUser
    void test1_1(Long campaignId, List<Long> lists) throws Exception {
        gson = new Gson();
        final String url = "/api/execution/getByCampaignIdAndExeIds";
        final ExecutionRequestDto executionRequestDto = ExecutionRequestDto.builder()
                .exeIds(lists)
                .campaignId(campaignId)
                .build();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url)
                .param("campaignId", String.valueOf(campaignId));

        for (Long exeId : lists) {
            requestBuilder.param("exeIds", String.valueOf(exeId));
        }

        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpectAll(
                status().isBadRequest(),
                jsonPath("errorMessage").value("잘못된 요청값 입니다.")
        ).andDo(print());
    }

    private static Stream<Arguments> nullBodies(){
        return Stream.of(
                Arguments.of(1L,List.of())
        );
    }

    @DisplayName("/api/execution/getByCampaignIdAndExeIds : Success")
    @Test
    @WithAuthUser
    void test1_2() throws Exception {
        gson = new Gson();
        final String url = "/api/execution/getByCampaignIdAndExeIds";
        List<Long> lists = List.of(1L, 2L, 3L);
        Long campaignId = 1L;

        doReturn(List.of(getExecution(1L, "option1"),
                getExecution(2L, "option2"),
                getExecution(3L, "option3"))).when(executionService)
                .getExecutionsByCampaignIdAndExeIds(any(Long.class), any(List.class));

        // exeIds 파라미터를 여러 개로 전달
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url)
                .param("campaignId", String.valueOf(campaignId));

        for (Long exeId : lists) {
            requestBuilder.param("exeIds", String.valueOf(exeId));
        }

        final ResultActions resultActions = mockMvc.perform(requestBuilder);

        resultActions.andExpectAll(
                status().isOk(),
                jsonPath("message").value("get Api success"),
                jsonPath("data[0].exeId").value(1L),
                jsonPath("data[0].exeProductName").value("option1")
        ).andDo(print());
    }

    public Execution getExecution(Long exeId, String name){
        return Execution.builder()
                .exeId(exeId)
                .exeProductName(name)
                .build();
    }
}
