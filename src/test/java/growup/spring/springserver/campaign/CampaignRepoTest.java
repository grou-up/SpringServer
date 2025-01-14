package growup.spring.springserver.campaign;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.repository.CampaignRepository;
import growup.spring.springserver.login.domain.Member;
import growup.spring.springserver.login.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class CampaignRepoTest {
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private MemberRepository memberRepository;

    Member member;

    @BeforeEach
    void setUp(){
        member = memberRepository.save(newMember());
    }

    @Test
    void success_createDB(){
        assertThat(campaignRepository).isNotNull();
    }

    @Test
    void success_saveData(){
        //given
        final Campaign input = newCampaign(member,"test1",1L);
        //when
        final Campaign data = campaignRepository.save(input);
        //then
        assertThat(data.getCampaignId()).isEqualTo(1L);
    }

    @Test
    void success_selectData(){
        //given
        final Campaign input = newCampaign(member,"test1",1L);
        campaignRepository.save(input);
        //when
        final Campaign data = campaignRepository.findByCampaignId(1L).orElseThrow(NullPointerException::new);
        //then
        assertThat(data.getCampaignId()).isEqualTo(1L);
    }

    @Test
    void success_selectByMemberEmail(){
        //given
        final Campaign input1 = newCampaign(member,"test1",1L);
        final Campaign input2 = newCampaign(member,"test2",2L);
        final Campaign input3 = newCampaign(member,"test3",3L);
        //when
        campaignRepository.save(input1);
        campaignRepository.save(input2);
        campaignRepository.save(input3);
        final List<Campaign> data = campaignRepository.findAllByMember(member);
        //then
        assertThat(data.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("findByCampaignIdANDEmail() : Success. 캠패인 단건 조회")
    void test3(){
        //given
        final Campaign input1 = newCampaign(member,"test1",1L);
        campaignRepository.save(input1);
        //whne
        final Campaign result = campaignRepository.findByCampaignIdANDEmail(1L,member.getEmail()).get();
        //then
        assertThat(result.getCampaignId()).isEqualTo(1L);
    }


    public Campaign newCampaign(Member member,String name,Long l){
        return Campaign.builder()
                .member(member)
                .camCampaignName(name)
                .campaignId(l)
                .camAdType("Test")
                .build();
    }

    public Member newMember(){
        return Member.builder().email("test@test.com").build();
    }

}
