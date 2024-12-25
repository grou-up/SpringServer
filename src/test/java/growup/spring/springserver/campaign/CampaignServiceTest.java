package growup.spring.springserver.campaign;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.dto.CampaignResponseDto;
import growup.spring.springserver.campaign.repository.CampaignRepository;
import growup.spring.springserver.campaign.service.CampaignService;
import growup.spring.springserver.login.domain.Member;
import growup.spring.springserver.login.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class CampaignServiceTest {
    @InjectMocks
    private CampaignService campaignService;
    @Mock
    private CampaignRepository campaignRepository;
    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("getMyCampaigns(): ErrorCase1.캠패인 목록이 없을 때")
    void test1(){
        //given
        doReturn(Optional.of(getMember())).when(memberRepository).findByEmail(any(String.class));
        doReturn(new ArrayList<Campaign>()).when(campaignRepository).findAllByMember(any(Member.class));
        //when
        final Exception result = assertThrows(NullPointerException.class,
                ()->campaignService.getMyCampaigns("test@test.com"));
        //then
        assertThat(result.getMessage()).isEqualTo("현재 등록된 캠패인이 없습니다");
    }

    @Test
    @DisplayName("getMyCampaigns(): ErrorCase2.해당 멤버를 찾지 못 할때")
    void test2(){
        //given
        doReturn(Optional.empty()).when(memberRepository).findByEmail(any(String.class));
        //when
        final NullPointerException result = assertThrows(NullPointerException.class,
               ()-> campaignService.getMyCampaigns("test@test.com"));
        //then
        assertThat(result.getMessage()).isEqualTo("해당 멤버가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("getMyCampaigns(): Success")
    void test3(){
        //given
        doReturn(Optional.of(getMember())).when(memberRepository).findByEmail(any(String.class));
        doReturn(List.of(
                    Campaign.builder().camCampaignName("campaign1").campaignId(1L).build(),
                    Campaign.builder().camCampaignName("campaign2").campaignId(2L).build(),
                    Campaign.builder().camCampaignName("campaign3").campaignId(3L).build())
        ).when(campaignRepository).findAllByMember(any(Member.class));
        //when
        List<CampaignResponseDto> result = campaignService.getMyCampaigns("test1@test.com");
        //then
        //TODO: 한 번에 테스트 할 수는 없을까?
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getTitle()).isEqualTo("campaign1");
        assertThat(result.get(1).getTitle()).isEqualTo("campaign2");
        assertThat(result.get(2).getTitle()).isEqualTo("campaign3");
        assertThat(result.get(0).getCampaignId()).isEqualTo(1L);
        assertThat(result.get(1).getCampaignId()).isEqualTo(2L);
        assertThat(result.get(2).getCampaignId()).isEqualTo(3L);
    }

    public Member getMember(){
        return Member.builder()
                .email("test@test.com")
                .build();
    }

}
