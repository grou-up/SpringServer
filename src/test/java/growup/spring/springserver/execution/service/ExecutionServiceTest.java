package growup.spring.springserver.execution.service;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.execution.domain.Execution;
import growup.spring.springserver.execution.repository.ExecutionRepository;
import org.junit.jupiter.api.BeforeEach;
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
public class ExecutionServiceTest {
    @Mock
    private ExecutionRepository executionRepository;
    @InjectMocks
    private ExecutionService executionService;

    private Campaign campaign;

    @BeforeEach
    void setUp(){
        campaign = Campaign.builder()
                .camCampaignName("test")
                .campaignId(1L)
                .build();
    }

    @DisplayName("getExecutionsByCampaignIdAndExeIds() : 빈값이 조회 결과일 때")
    @Test
    void test1_1(){
        //given
        doReturn(List.of()).when(executionRepository).findByCampaignIdAndExeId(any(Long.class),any(List.class));
        final List<Long> request = List.of(1L,2L,3L);
        //when
        final List<Execution> result = executionService.getExecutionsByCampaignIdAndExeIds(1L,request);
        //then
        assertThat(result).hasSize(0);
    }

    @DisplayName("getExecutionsByCampaignIdAndExeIds() : 빈값을 조회 시")
    @Test
    void test1_2(){
        //given
        doReturn(List.of()).when(executionRepository).findByCampaignIdAndExeId(any(Long.class),any(List.class));
        final List<Long> request = List.of();
        //when
        final List<Execution> result = executionService.getExecutionsByCampaignIdAndExeIds(1L,request);
        //then
        assertThat(result).hasSize(0);
    }

    @DisplayName("getExecutionsByCampaignIdAndExeIds() : Success")
    @Test
    void test1_3(){
        //given
        doReturn(List.of(newExecution(campaign,1L,"details","name1"),
                newExecution(campaign,2L,"details","name2"),
                newExecution(campaign,3L,"details","name3"))).when(executionRepository).findByCampaignIdAndExeId(any(Long.class),any(List.class));
        final List<Long> request = List.of(1L,2L,3L);
        //when
        final List<Execution> result = executionService.getExecutionsByCampaignIdAndExeIds(1L,request);
        //then
        assertThat(result).hasSize(3);
    }

    public Execution newExecution(Campaign campaign, long l, String detail, String name) {
        return Execution.builder()
                .exeId(l)
                .exeDetailCategory(detail)
                .exeProductName(name)
                .campaign(campaign)
                .build();
    }



}
