package growup.spring.springserver.margin.service;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.repository.CampaignRepository;
import growup.spring.springserver.exception.campaign.CampaignNotFoundException;
import growup.spring.springserver.exception.login.MemberNotFoundException;
import growup.spring.springserver.login.domain.Member;
import growup.spring.springserver.login.repository.MemberRepository;
import growup.spring.springserver.margin.TypeChangeMargin;
import growup.spring.springserver.margin.domain.Margin;
import growup.spring.springserver.margin.dto.MarginSummaryResponseDto;
import growup.spring.springserver.margin.repository.MarginRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class MarginService {

    private final MarginRepository marginRepository;
    private final MemberRepository memberRepository;
    private final CampaignRepository campaignRepository;

    public List<MarginSummaryResponseDto> getCampaignAllSales(String email, LocalDate targetDate) {
        LocalDate difTargetDate = targetDate.minusDays(1);
        Member member = memberRepository.findByEmail(email).orElseThrow(
                MemberNotFoundException::new
        );
        List<Campaign> campaignList = campaignRepository.findAllByMember(member);
        if(campaignList.isEmpty()) throw new CampaignNotFoundException();

        List<Long> campaignIds = campaignList.stream()
                .map(Campaign::getCampaignId)
                .toList();

        List<Margin> margins = marginRepository.findByCampaignIdsAndDates(campaignIds, List.of(targetDate, difTargetDate));

        Map<Long, List<Margin>> marginMap = margins.stream()
                .collect(Collectors.groupingBy(m -> m.getCampaign().getCampaignId()));

        List<MarginSummaryResponseDto> summaries = new ArrayList<>();

        for (Campaign campaign : campaignList) {

            // 오늘과 어제 데이터 필터링
            Margin todayMargin = getMarginForDateOrDefault(marginMap, campaign, targetDate);
            Margin yesterdayMargin = getMarginForDateOrDefault(marginMap, campaign, difTargetDate);

            MarginSummaryResponseDto summary = TypeChangeMargin.entityToMarginSummaryResponseDto(
                    campaign, todayMargin, yesterdayMargin);

            summaries.add(summary);
        }

        return summaries;
    }
    private Margin getMarginForDateOrDefault(Map<Long, List<Margin>> marginMap, Campaign campaign, LocalDate date) {
        Long campaignId = campaign.getCampaignId();
        List<Margin> campaignMargins = marginMap.getOrDefault(campaignId, new ArrayList<>());

        return campaignMargins.stream()
                .filter(m -> m.getMarDate().equals(date))
                .findFirst()
                .orElse(TypeChangeMargin.createDefaultMargin(campaign, date));
    }
}