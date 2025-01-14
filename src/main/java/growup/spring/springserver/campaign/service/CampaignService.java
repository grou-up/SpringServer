package growup.spring.springserver.campaign.service;

import growup.spring.springserver.campaign.TypeChangeCampaign;
import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.dto.CampaignResponseDto;
import growup.spring.springserver.campaign.repository.CampaignRepository;
import growup.spring.springserver.exception.campaign.CampaignNotFoundException;
import growup.spring.springserver.exception.login.MemberNotFoundException;
import growup.spring.springserver.login.domain.Member;
import growup.spring.springserver.login.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CampaignRepository campaignRepository;

    //TODO: 예외 클래스 변경 및 반환 데이터에 ID로 넣어야할까 고민중.
    public List<CampaignResponseDto> getMyCampaigns(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(
                MemberNotFoundException::new
        );
        List<Campaign> campaignList = campaignRepository.findAllByMember(member);
        if(campaignList.isEmpty()) throw new CampaignNotFoundException();
        return campaignList.stream().map(TypeChangeCampaign::entityToResponseDto).toList();
    }

    public Campaign getMyCampaign(Long campaignId,String email){
        return campaignRepository.findByCampaignIdANDEmail(campaignId,email).orElseThrow(
                CampaignNotFoundException::new
        );
    }
}
