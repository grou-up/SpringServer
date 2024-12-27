package growup.spring.springserver.exclusionKeyword.controller;

import com.nimbusds.jose.shaded.gson.Gson;
import growup.spring.springserver.annotation.WithAuthUser;
import growup.spring.springserver.exclusionKeyword.dto.ExclusionKeywordRequestDto;
import growup.spring.springserver.exclusionKeyword.dto.ExclusionKeywordResponseDto;
import growup.spring.springserver.exclusionKeyword.service.ExclusionKeywordService;
import growup.spring.springserver.global.config.JwtTokenProvider;
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

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ExclusionKeywordController.class)
public class ExclusionKeywordControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private Gson gson;
    @MockBean
    private ExclusionKeywordService exclusionKeywordService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @DisplayName("addExclusionKeyword() : Error 1. 캠패인 ID 누락 시")
    @Test
    @WithAuthUser
    void test1_1() throws Exception {
        gson = new Gson();
        final String url = "/api/exclusionKeyword/addExclusionKeyword";
        ResultActions resultActions = mockMvc.perform(post(url)
                .content(gson.toJson(ExclusionKeywordRequestDto.builder().exclusionKeyword("key").build()))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())); // 403 에러 해결을 위해 추가
        resultActions.andExpectAll(
                status().isBadRequest(),
                jsonPath("errorMessage").value("조회할 캠패인 ID를 입력해주세요")
        ).andDo(print());
    }

    @DisplayName("addExclusionKeyword() : Error 2. 제외 키워드 누락 시")
    @Test
    @WithAuthUser
    void test1_2() throws Exception {
        //when
        gson = new Gson();
        final String url = "/api/exclusionKeyword/addExclusionKeyword";
        //given
        ResultActions resultActions = mockMvc.perform(post(url)
                .content(gson.toJson(ExclusionKeywordRequestDto.builder().campaignId(1L).build()))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())); // 403 에러 해결을 위해 추가
        //then
        resultActions.andExpectAll(
                status().isBadRequest(),
                jsonPath("errorMessage").value("제외 키워드를 입력해주세요")
        ).andDo(print());
    }

    @DisplayName("addExclusionKeyword() : Success 3. 제외 키워드 누락 시")
    @Test
    @WithAuthUser
    void test1_3() throws Exception {
        //when
        gson = new Gson();
        final String url = "/api/exclusionKeyword/addExclusionKeyword";
        //given
        doReturn(ExclusionKeywordResponseDto.builder()
                .exclusionKeyword("key")
                .campaignId(1L)
                .build()).when(exclusionKeywordService).addExclusionKeyword(1L,"key");
        ResultActions resultActions = mockMvc.perform(post(url)
                .content(gson.toJson(ExclusionKeywordRequestDto.builder().exclusionKeyword("key").campaignId(1L).build()))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())); // 403 에러 해결을 위해 추가
        //then
        resultActions.andExpectAll(
                status().isOk(),
                jsonPath("message").value("success : add exclusionKeyword"),
                jsonPath("data.exclusionKeyword").value("key"),
                jsonPath("data.campaignId").value(1L)
        ).andDo(print());
    }

    @DisplayName("addExclusionKeyword() : Error 1. Param 데이터 누락")
    @ParameterizedTest
    @MethodSource("inCorrectUrlProvider")
    @WithAuthUser
    void test2_1(String url) throws Exception {
        //when
        doThrow(IllegalArgumentException.class).when(exclusionKeywordService).deleteExclusionKeyword(any(Long.class),any(String.class));
        //then
        ResultActions resultActions = mockMvc.perform(delete(url)
                .with(csrf())); // 403 에러 해결을 위해 추가
        resultActions.andExpectAll(
                status().isBadRequest()
        ).andDo(print());
    }
    static Stream<String> inCorrectUrlProvider() {
        return Stream.of("/api/exclusionKeyword/remove?campaignId=1&exclusionKeyword=",
                "/api/exclusionKeyword/remove?exclusionKeyword=exclusionKey",
                "/api/exclusionKeyword/remove?campaignId=data&exclusionKeyword=exclusionKey");
    }

    @DisplayName("addExclusionKeyword() : Success")
    @Test
    @WithAuthUser
    void test2_2() throws Exception {
        //when
        final String url = "/api/exclusionKeyword/remove?campaignId=1101&exclusionKeyword=exclusionKey";
        doReturn(true).when(exclusionKeywordService).deleteExclusionKeyword(any(Long.class),any(String.class));
        //then
        ResultActions resultActions = mockMvc.perform(delete(url)
                .with(csrf())); // 403 에러 해결을 위해 추가
        resultActions.andExpectAll(
                status().isOk(),
                jsonPath("message").value("success : remove")
        ).andDo(print());
    }

    @DisplayName("getExclusionKeywords() : 파라미터 값 누락")
    @ParameterizedTest
    @WithAuthUser
    @MethodSource("incorrectGetExKeysURl")
    void test3_1(String url) throws Exception {
        //when
        //given
        ResultActions resultActions = mockMvc.perform(get(url)
                .with(csrf()));
        //then
        resultActions.andExpectAll(
                status().isBadRequest()
        ).andDo(print());
    }
    static Stream<String> incorrectGetExKeysURl(){
        return Stream.of("/api/exclusionKeyword/getExclusionKeywords",
                "/api/exclusionKeyword/getExclusionKeywords?campaignId=",
                "/api/exclusionKeyword/getExclusionKeywords?campaignId=noLong"
                );
    }

    @DisplayName("getExclusionKeywords() : Success")
    @Test
    @WithAuthUser
    void test3_2() throws Exception {
        //when
        final String url = "/api/exclusionKeyword/getExclusionKeywords?campaignId=1";
        doReturn(List.of("exK1","exK2","exK3")).when(exclusionKeywordService).getExclusionKeywords(any(Long.class));
        //given
        ResultActions resultActions = mockMvc.perform(get(url));
        //then
        resultActions.andExpectAll(
                status().isOk(),
                jsonPath("data[0]").value("exK1"),
                jsonPath("data[1]").value("exK2"),
                jsonPath("data[2]").value("exK3")
        );

    }

}
