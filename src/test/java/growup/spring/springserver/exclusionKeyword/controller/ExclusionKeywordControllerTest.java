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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void test2() throws Exception {
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
    void test3() throws Exception {
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
    void test4() throws Exception {
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
}
