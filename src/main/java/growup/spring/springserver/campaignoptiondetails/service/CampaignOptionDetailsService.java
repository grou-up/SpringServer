package growup.spring.springserver.campaignoptiondetails.service;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.repository.CampaignRepository;
import growup.spring.springserver.campaignoptiondetails.TypeChangeCampaignOptionDetails;
import growup.spring.springserver.campaignoptiondetails.domain.CampaignOptionDetails;
import growup.spring.springserver.campaignoptiondetails.dto.CampaignOptionDetailsResponseDto;
import growup.spring.springserver.campaignoptiondetails.dto.CampaignSummaryResponseDto;
import growup.spring.springserver.campaignoptiondetails.repository.CampaignOptionDetailsRepository;
import growup.spring.springserver.exception.InvalidDateFormatException;
import growup.spring.springserver.exception.campaign.CampaignNotFoundException;
import growup.spring.springserver.exception.campaignoptiondetails.CampaignOptionDataNotFoundException;
import growup.spring.springserver.exception.campaignoptiondetails.CampaignOptionNotFoundException;
import growup.spring.springserver.exception.login.MemberNotFoundException;
import growup.spring.springserver.execution.repository.ExecutionRepository;
import growup.spring.springserver.login.domain.Member;
import growup.spring.springserver.login.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class CampaignOptionDetailsService {
    private final CampaignOptionDetailsRepository campaignOptionDetailsRepository;
    private final ExecutionRepository executionRepository;
    private final MemberRepository memberRepository;
    private final CampaignRepository campaignRepository;

    public List<CampaignOptionDetails> getRawCampaignDetails(LocalDate start, LocalDate end, long id) {
        if (start.isAfter(end)) {
            throw new InvalidDateFormatException();
        }
        List<Long> byCampaignCampaignIds = executionRepository.findExecutionIdsByCampaignId(id);
        if (byCampaignCampaignIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<CampaignOptionDetails> byExecutionIdsAndDateRange = campaignOptionDetailsRepository.findByExecutionIdsAndDateRange(
                byCampaignCampaignIds, start, end);
        if (byExecutionIdsAndDateRange.isEmpty()) {
            return Collections.emptyList();
        }
        return byExecutionIdsAndDateRange;
    }

    public List<CampaignSummaryResponseDto> getCampaignAllSales(LocalDate date, String email) {

        LocalDate yesterday = date.minusDays(1);

        // 멤버 확인 및 캠페인 가져오기
        Member member = memberRepository.findByEmail(email).orElseThrow(
                MemberNotFoundException::new
        );
        List<Campaign> campaignList = campaignRepository.findAllByMember(member);
        if (campaignList.isEmpty()) throw new CampaignNotFoundException();
        List<CampaignSummaryResponseDto> summaries = new ArrayList<>();

        for (Campaign campaign : campaignList) {
            List<CampaignOptionDetails> todayDetails = getRawCampaignDetails(date, date, campaign.getCampaignId());
            List<CampaignOptionDetails> yesterdayDetails = getRawCampaignDetails(yesterday, yesterday, campaign.getCampaignId());

            CampaignSummaryResponseDto summary = TypeChangeCampaignOptionDetails.entityToSummaryResponseDto(
                    date,
                    campaign,
                    todayDetails,
                    yesterdayDetails
            );
            summaries.add(summary);
        }

        return summaries;
    }

    public List<CampaignOptionDetailsResponseDto> getCampaignDetailsByCampaignsIds(LocalDate start, LocalDate end, long id) {
        List<CampaignOptionDetails> data = getRawCampaignDetails(start, end, id);

        HashMap<Long, CampaignOptionDetailsResponseDto> map = new HashMap<>();
        for(CampaignOptionDetails details : data){

            if(map.containsKey(details.getExecution().getExeId())){
                map.get(details.getExecution().getExeId()).update(details);
                continue;
            }
            map.put(details.getExecution().getExeId(), TypeChangeCampaignOptionDetails.entityToResponseDto(details));
        }

        List<CampaignOptionDetailsResponseDto> campaignOptionDetailsResponseDtos = new ArrayList<>();
        for(Long key : map.keySet()){
            campaignOptionDetailsResponseDtos.add(map.get(key));
        }
        return campaignOptionDetailsResponseDtos;
    }
}