package growup.spring.springserver.execution.repository;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.repository.CampaignRepository;
import growup.spring.springserver.execution.domain.Execution;
import growup.spring.springserver.login.domain.Member;
import growup.spring.springserver.login.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ExecutionRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ExecutionRepository executionRepository;

    Member member;
    Campaign campaign, campaign2;
    Execution execution1, execution2,execution3;

    @BeforeEach
    void setup() {
        member = memberRepository.save(newMember());
        campaign = campaignRepository.save(newCampaign(member,1L));
        campaign2 = campaignRepository.save(newCampaign(member,2L));

    }

    @Test
    void success_createDB() {
        assertThat(executionRepository).isNotNull();
    }

    @Test
    @DisplayName("findByCampaignCampaignId(): 특정 Campaign ID와 관련된 Execution 반환")
    void findByCampaignCampaignIdReturnsExecutions() {
        // Given
        execution1 = executionRepository.save(newExecution(campaign, 91130550615L, "블랙", "안경 케이스 - 블랙"));
        execution2 = executionRepository.save(newExecution(campaign, 91130550622L, "화이트", "안경 케이스 - 화이트"));
        execution3 = executionRepository.save(newExecution(campaign2, 91130550623L, "블루", "안경 케이스 - 블루"));

        // When
        List<Long> result = executionRepository.findExecutionIdsByCampaignId(campaign.getCampaignId());

        // Then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("findByCampaignCampaignId(): 없는 값")
    void findByCampaignCampaignIdReturnsEmptyOptional() {
        // When
        List<Long> result = executionRepository.findExecutionIdsByCampaignId(999999L);

        // Then
        assertThat(result.isEmpty()); // 내부 리스트가 비어 있는지 확인
    }


    public Member newMember() {
        return Member.builder().email("test@test.com").build();
    }

    public Campaign newCampaign(Member member,Long campaignId) {
        return Campaign.builder()
                .member(member)
                .camCampaignName("방한 마스크")
                .campaignId(campaignId)
                .camAdType("Test")
                .build();
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