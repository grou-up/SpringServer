package growup.spring.springserver.execution.service;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.execution.domain.Execution;
import growup.spring.springserver.execution.dto.ExecutionMarginResDto;
import growup.spring.springserver.execution.repository.ExecutionRepository;
import growup.spring.springserver.login.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ExecutionServiceTest {

    @InjectMocks
    private ExecutionService executionService;

    @Mock
    ExecutionRepository executionRepository;

    @Test
    @DisplayName("executionMarginResDto() ErrorCase1. 캠패인 존재 하지 않을때 ")
    void executionMarginResDto_ErrorCase1() {
        // given
        doReturn(List.of()).when(executionRepository).findExecutionByCampaignId(any(Long.class));

        //when
        List<ExecutionMarginResDto> executionMarginResDtos = executionService.getMyExecutionData(any(Long.class));

        //then
        assertThat(executionMarginResDtos).isEmpty();

    }

    @Test
    @DisplayName("executionMarginResDto() SuccessCase1. executionMarginResDto 반환 완료")
    void executionMarginResDto_SuccessCase1() {
        // given
        List<Execution> mockExecutions = List.of(
                newExecution(1L, 1L, "빨강색", "모자", getCampaign(1L, "모자")),
                newExecution(2L, 2L, "파랑색", "모자1", getCampaign(1L, "모자"))
        );
        doReturn(mockExecutions)
                .when(executionRepository).findExecutionByCampaignId(any(Long.class));
        // when
        List<ExecutionMarginResDto> executionMarginResDtos = executionService.getMyExecutionData(1L);
        System.out.println("executionMarginResDtos = " + executionMarginResDtos);

        // then
        assertThat(executionMarginResDtos).hasSize(2);
        assertThat(executionMarginResDtos.get(0)).extracting("exeId").isEqualTo(1L);
        assertThat(executionMarginResDtos.get(0)).extracting("exeDetailCategory").isEqualTo("빨강색");

        assertThat(executionMarginResDtos.get(1)).extracting("exeId").isEqualTo(2L);
        assertThat(executionMarginResDtos.get(1)).extracting("exeDetailCategory").isEqualTo("파랑색");
    }

    public Member getMember() {
        return Member.builder()
                .email("test@test.com")
                .build();
    }

    public Execution newExecution(long l, long executionid, String detail, String name, Campaign campaign) {
        return Execution.builder()
                .id(l)
                .exeId(executionid)
                .exeDetailCategory(detail)
                .exeProductName(name)
                .campaign(campaign)
                .build();
    }

    public Campaign getCampaign(Long id, String title) {
        return Campaign.builder()
                .campaignId(id)
                .camCampaignName(title)
                .build();
    }
}