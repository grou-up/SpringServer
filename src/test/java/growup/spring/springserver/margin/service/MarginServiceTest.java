package growup.spring.springserver.margin.service;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.repository.CampaignRepository;
import growup.spring.springserver.exception.campaign.CampaignNotFoundException;
import growup.spring.springserver.login.domain.Member;
import growup.spring.springserver.login.repository.MemberRepository;
import growup.spring.springserver.margin.domain.Margin;
import growup.spring.springserver.margin.dto.MarginSummaryResponseDto;
import growup.spring.springserver.margin.repository.MarginRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MarginServiceTest {
    @InjectMocks
    private MarginService marginService;
    @Mock
    private MarginRepository marginRepository;
    @Mock
    private CampaignRepository campaignRepository;
    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("getCampaignAllSales(): ErrorCase1.캠패인 목록이 없을 때")
    void test1() {
        //given
        doReturn(Optional.of(getMember())).when(memberRepository).findByEmail(any(String.class));
        doReturn(new ArrayList<Campaign>()).when(campaignRepository).findAllByMember(any(Member.class));
        //when
        final Exception result = assertThrows(CampaignNotFoundException.class,
                ()->marginService.getCampaignAllSales("test@test.com", LocalDate.of(2024,11,11)));
        //then
        assertThat(result.getMessage()).isEqualTo("현재 등록된 캠페인이 없습니다.");
    }

    // 날짜,
    @Test
    @DisplayName("getCampaignAllSales(): 데이터가 없는 경우 기본값 처리")
    void testMissingData() {
        // Given
        LocalDate today = LocalDate.of(2024, 11, 11);
        LocalDate yesterday = today.minusDays(1);

        List<Campaign> campaigns = List.of(
                Campaign.builder().campaignId(1L).camCampaignName("Campaign 1").build(),
                Campaign.builder().campaignId(2L).camCampaignName("Campaign 2").build()
        );

        List<Long> campaignIds = campaigns.stream()
                .map(Campaign::getCampaignId)
                .toList();

        // 오늘 데이터가 없는 경우
        List<Margin> yesterdayMargins = List.of(
                newMargin(yesterday, campaigns.get(0), 90L, 180.0, 45L),
                newMargin(yesterday, campaigns.get(1), 120L, 240.0, 60L)
        );

        doReturn(Optional.of(getMember())).when(memberRepository).findByEmail("test@test.com");
        doReturn(campaigns).when(campaignRepository).findAllByMember(any(Member.class));
        doReturn(yesterdayMargins).when(marginRepository).findByCampaignIdsAndDates(campaignIds, List.of(today, yesterday));

        // When
        List<MarginSummaryResponseDto> result = marginService.getCampaignAllSales("test@test.com", today);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTodaySales()).isEqualTo(0.0);
        assertThat(result.get(0).getYesterdaySales()).isEqualTo(180.0);
        assertThat(result.get(0).getDifferentSales()).isEqualTo(-180.0);

        assertThat(result.get(1).getTodaySales()).isEqualTo(0.0);
        assertThat(result.get(1).getYesterdaySales()).isEqualTo(240.0);
        assertThat(result.get(1).getDifferentSales()).isEqualTo(-240.0);
    }

    @Test
    @DisplayName("getCampaignAllSales(): 캠페인 1은 어제 데이터 없음, 캠페인 2는 오늘 데이터 없음")
    void testVariousDataCombinations() {
        // Given
        LocalDate today = LocalDate.of(2024, 11, 11);
        LocalDate yesterday = today.minusDays(1);

        List<Campaign> campaigns = List.of(
                Campaign.builder().campaignId(1L).camCampaignName("Campaign 1").build(),
                Campaign.builder().campaignId(2L).camCampaignName("Campaign 2").build()
        );

        List<Long> campaignIds = campaigns.stream()
                .map(Campaign::getCampaignId)
                .toList();

        List<Margin> mixedMargins = List.of(
                newMargin(today, campaigns.get(0), 100L, 200.0, 50L), // Campaign 1: 오늘 데이터
                newMargin(yesterday, campaigns.get(1), 120L, 240.0, 60L) // Campaign 2: 어제 데이터
        );

        doReturn(Optional.of(getMember())).when(memberRepository).findByEmail("test@test.com");
        doReturn(campaigns).when(campaignRepository).findAllByMember(any(Member.class));
        doReturn(mixedMargins).when(marginRepository).findByCampaignIdsAndDates(campaignIds, List.of(today, yesterday));

        // When
        List<MarginSummaryResponseDto> result = marginService.getCampaignAllSales("test@test.com", today);

        // Then
        assertThat(result).hasSize(2);

        // Campaign 1: 어제 데이터 없음
        assertThat(result.get(0).getTodaySales()).isEqualTo(200.0);
        assertThat(result.get(0).getYesterdaySales()).isEqualTo(0.0);
        assertThat(result.get(0).getDifferentSales()).isEqualTo(200.0);

        // Campaign 2: 오늘 데이터 없음
        assertThat(result.get(1).getTodaySales()).isEqualTo(0.0);
        assertThat(result.get(1).getYesterdaySales()).isEqualTo(240.0);
        assertThat(result.get(1).getDifferentSales()).isEqualTo(-240.0);
    }

    @Test
    @DisplayName("getCampaignAllSales(): Success - Dto 매핑 테스트")
    void testGetCampaignAllSalesSuccess() {
        // Given
        LocalDate today = LocalDate.of(2024, 11, 11);
        LocalDate yesterday = today.minusDays(1);

        Member member = getMember();

        List<Campaign> campaigns = List.of(
                Campaign.builder().campaignId(1L).camCampaignName("Campaign 1").build(),
                Campaign.builder().campaignId(2L).camCampaignName("Campaign 2").build()
        );

        List<Margin> margins = List.of(
                newMargin(today, campaigns.get(0), 100L, 200.0, 50L), // Campaign 1: 오늘 데이터
                newMargin(yesterday, campaigns.get(0), 90L, 180.0, 45L), // Campaign 1: 어제 데이터
                newMargin(today, campaigns.get(1), 150L, 300.0, 75L), // Campaign 2: 오늘 데이터
                newMargin(yesterday, campaigns.get(1), 120L, 240.0, 60L) // Campaign 2: 어제 데이터
        );

        doReturn(Optional.of(member)).when(memberRepository).findByEmail("test@test.com");
        doReturn(campaigns).when(campaignRepository).findAllByMember(any(Member.class));
        doReturn(margins).when(marginRepository).findByCampaignIdsAndDates(
                campaigns.stream().map(Campaign::getCampaignId).toList(),
                List.of(today, yesterday)
        );

        // When
        List<MarginSummaryResponseDto> result = marginService.getCampaignAllSales("test@test.com", today);

        // Then
        assertThat(result).hasSize(2);

        MarginSummaryResponseDto campaign1 = result.get(0);
        assertThat(campaign1.getCampaignId()).isEqualTo(1L);
        assertThat(campaign1.getCampaignName()).isEqualTo("Campaign 1");
        assertThat(campaign1.getYesterdaySales()).isEqualTo(180.0); // 어제 광고비
        assertThat(campaign1.getTodaySales()).isEqualTo(200.0); // 오늘 광고비
        assertThat(campaign1.getDifferentSales()).isEqualTo(20.0); // 차액

        MarginSummaryResponseDto campaign2 = result.get(1);
        assertThat(campaign2.getCampaignId()).isEqualTo(2L);
        assertThat(campaign2.getCampaignName()).isEqualTo("Campaign 2");
        assertThat(campaign2.getYesterdaySales()).isEqualTo(240.0); // 어제 광고비
        assertThat(campaign2.getTodaySales()).isEqualTo(300.0); // 오늘 광고비
        assertThat(campaign2.getDifferentSales()).isEqualTo(60.0); // 차액

        // Verify interactions
        verify(memberRepository).findByEmail("test@test.com");
        verify(campaignRepository).findAllByMember(any(Member.class));
        verify(marginRepository).findByCampaignIdsAndDates(
                campaigns.stream().map(Campaign::getCampaignId).toList(),
                List.of(today, yesterday)
        );
    }



    private Margin newMargin(LocalDate date, Campaign campaign, Long impressions, Double cost, Long conversions) {
        return Margin.builder()
                .marDate(date)
                .campaign(campaign)
                .marImpressions(impressions)
                .marAdCost(cost)
                .marAdConversionSales(conversions)
                .build();
    }

    public Member getMember() {
        return Member.builder()
                .email("test@test.com")
                .build();
    }
}