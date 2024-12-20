package growup.spring.springserver.campaign;

import growup.spring.springserver.annotation.WithAuthUser;
import growup.spring.springserver.global.config.JwtTokenProvider;
import growup.spring.springserver.global.fillter.JwtAuthFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
        final String url = "/campaign/getMyCampaigns";
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url));
        resultActions.andDo(print()).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("getMyCampaigns : Success1.접근 성공")
    @WithAuthUser
    void test2() throws Exception {
        final String url = "/campaign/getMyCampaigns";
        doReturn(List.of()).when(campaignService).getMyCampaigns("test@test.com");
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url));
        resultActions.andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

}
