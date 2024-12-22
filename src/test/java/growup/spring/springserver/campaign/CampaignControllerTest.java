package growup.spring.springserver.campaign;

import com.nimbusds.jose.shaded.gson.Gson;
import growup.spring.springserver.annotation.WithAuthUser;
import growup.spring.springserver.global.config.JwtTokenProvider;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CampaignController.class)
public class CampaignControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignService campaignService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    @DisplayName("getMyCampaigns : ErrorCase1.인가되지 않은 사용자 접근")
    void test1() throws Exception {
        final String url = "/api/campaign/getMyCampaigns";
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url));
        resultActions.andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("getMyCampaigns : Success1.접근 성공")
    @WithAuthUser
    void test2() throws Exception {
        Gson gson = new Gson();
        final String url = "/api/campaign/getMyCampaigns";
        doReturn(List.of("name1","name2","name3")).when(campaignService).getMyCampaigns("test@test.com");
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new ArrayList<String>())));

        resultActions.andExpectAll(
                status().isOk(),
                jsonPath("message").value("success : load campaign name list"),
                jsonPath("data").isArray(), // data가 배열인지 확인
                jsonPath("data[0]").value("name1"), // 첫 번째 요소 검증
                jsonPath("data[1]").value("name2"), // 두 번째 요소 검증
                jsonPath("data[2]").value("name3")  // 세 번째 요소 검증
        ).andDo(print());
    }
}
