package growup.spring.springserver.margin.service;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.repository.CampaignRepository;
import growup.spring.springserver.exception.campaign.CampaignNotFoundException;
import growup.spring.springserver.login.domain.Member;
import growup.spring.springserver.login.repository.MemberRepository;
import growup.spring.springserver.margin.domain.Margin;
import growup.spring.springserver.margin.dto.DailyAdSummaryDto;
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
import static org.mockito.Mockito.*;

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
        final CampaignNotFoundException result = assertThrows(CampaignNotFoundException.class,
                ()->marginService.getCampaignAllSales("test@test.com", LocalDate.of(2024,11,11)));
        //then
        assertThat(result.getMessage()).isEqualTo("현재 등록된 캠페인이 없습니다.");
    }

    // 날짜,
    @Test
    @DisplayName("getCampaignAllSales(): 오늘 데이터가 없는 경우 기본값 처리")
    void test2() {
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

        // 어제 데이터만 존재
        List<Margin> yesterdayMargins = List.of(
                newMargin(yesterday, campaigns.get(0), 180.0),
                newMargin(yesterday, campaigns.get(1),  240.0)
        );

        // Mock 설정
        doReturn(Optional.of(getMember())).when(memberRepository).findByEmail("test@test.com");
        doReturn(campaigns).when(campaignRepository).findAllByMember(any(Member.class));
        doAnswer(invocation -> {
            List<Long> ids = invocation.getArgument(0); // 실제 호출된 값
            LocalDate from = invocation.getArgument(1);
            LocalDate to = invocation.getArgument(2);
            System.out.println("ids = " + ids);
            System.out.println("from = " + from);

            // 테스트 데이터에 `ids`가 포함된 조건 추가
            return yesterdayMargins.stream()
                    .filter(margin -> ids.contains(margin.getCampaign().getCampaignId()) &&
                            (margin.getMarDate().isEqual(from) || margin.getMarDate().isAfter(from)) &&
                            (margin.getMarDate().isEqual(to) || margin.getMarDate().isBefore(to)))
                    .toList();
        }).when(marginRepository).findByCampaignIdsAndDates(eq(campaignIds), any(LocalDate.class), any(LocalDate.class));


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
    void test3() {
        // Given
        LocalDate today = LocalDate.of(2024, 11, 11);
        LocalDate yesterday = today.minusDays(1);

        List<Campaign> campaigns = List.of(
                Campaign.builder().campaignId(1L).camCampaignName("Campaign 1").build(),
                Campaign.builder().campaignId(2L).camCampaignName("Campaign 2").build()
        );

        List<Margin> mixedMargins = List.of(
                newMargin(today, campaigns.get(0), 200.0), // Campaign 1 오늘 매출
                newMargin(yesterday, campaigns.get(1), 240.0) // Campaign 2 어제 매출
        );

        // Mock 설정
        doReturn(Optional.of(getMember())).when(memberRepository).findByEmail("test@test.com");
        doReturn(campaigns).when(campaignRepository).findAllByMember(any(Member.class));
        doReturn(mixedMargins).when(marginRepository).findByCampaignIdsAndDates(
                campaigns.stream().map(Campaign::getCampaignId).toList(),
                yesterday,
                today
        );

        // When
        List<MarginSummaryResponseDto> result = marginService.getCampaignAllSales("test@test.com", today);

        // Then
        assertThat(result).hasSize(2);

        // Campaign 1: 어제 데이터 없음
        assertThat(result.get(0).getTodaySales()).isEqualTo(200.0); // 오늘 매출
        assertThat(result.get(0).getYesterdaySales()).isEqualTo(0.0); // 어제 매출 없음
        assertThat(result.get(0).getDifferentSales()).isEqualTo(200.0); // 차이

        // Campaign 2: 오늘 데이터 없음
        assertThat(result.get(1).getTodaySales()).isEqualTo(0.0); // 오늘 매출 없음
        assertThat(result.get(1).getYesterdaySales()).isEqualTo(240.0); // 어제 매출
        assertThat(result.get(1).getDifferentSales()).isEqualTo(-240.0); // 차이
    }

    @Test
    @DisplayName("getCampaignAllSales(): Success - Dto 매핑 테스트 with getMarginForDateOrDefault")
    void test4() {
        // Given
        LocalDate today = LocalDate.of(2024, 11, 11);
        LocalDate yesterday = today.minusDays(1);

        List<Campaign> campaigns = List.of(
                Campaign.builder().campaignId(1L).camCampaignName("Campaign 1").build(),
                Campaign.builder().campaignId(2L).camCampaignName("Campaign 2").build()
        );

        List<Margin> margins = List.of(
                newMargin(today, campaigns.get(0), 200.0), // Campaign 1 오늘 매출
                newMargin(yesterday, campaigns.get(0), 180.0), // Campaign 1 어제 매출
                newMargin(today, campaigns.get(1), 300.0), // Campaign 2 오늘 매출
                newMargin(yesterday, campaigns.get(1), 240.0) // Campaign 2 어제 매출
        );

        // Mock 설정
        doReturn(Optional.of(getMember())).when(memberRepository).findByEmail("test@test.com");
        doReturn(campaigns).when(campaignRepository).findAllByMember(any(Member.class));
        doReturn(margins).when(marginRepository).findByCampaignIdsAndDates(
                campaigns.stream().map(Campaign::getCampaignId).toList(),
                yesterday, today
        );

        // When
        List<MarginSummaryResponseDto> result = marginService.getCampaignAllSales("test@test.com", today);

        // Then
        assertThat(result).hasSize(2);

        MarginSummaryResponseDto campaign1 = result.get(0);
        assertThat(campaign1.getCampaignId()).isEqualTo(1L);
        assertThat(campaign1.getYesterdaySales()).isEqualTo(180.0); // 어제 매출
        assertThat(campaign1.getTodaySales()).isEqualTo(200.0); // 오늘 매출
        assertThat(campaign1.getDifferentSales()).isEqualTo(20.0); // 차이

        MarginSummaryResponseDto campaign2 = result.get(1);
        assertThat(campaign2.getCampaignId()).isEqualTo(2L);
        assertThat(campaign2.getYesterdaySales()).isEqualTo(240.0); // 어제 매출
        assertThat(campaign2.getTodaySales()).isEqualTo(300.0); // 오늘 매출
        assertThat(campaign2.getDifferentSales()).isEqualTo(60.0); // 차이
    }

    @Test
    @DisplayName("findByCampaignIdsAndDates(): Success - 7일간 데이터 요약")
    void test5_findByCampaignIdsAndDates() {
        // Given
        LocalDate today = LocalDate.of(2024, 11, 11);
        LocalDate sevenDaysAgo = today.minusDays(7);

        List<Campaign> campaigns = List.of(
                Campaign.builder().campaignId(1L).camCampaignName("Campaign 1").build(),
                Campaign.builder().campaignId(2L).camCampaignName("Campaign 2").build()
        );

        // 7일 데이터를 생성 (DailyAdSummaryDto는 Mock된 결과로 사용)
        List<DailyAdSummaryDto> dailySummaries = List.of(
                new DailyAdSummaryDto(today, 200.0, 200.0, 1.0), // 오늘 데이터
                new DailyAdSummaryDto(sevenDaysAgo, 180.0, 180.0, 1.0) // 7일 전 데이터
        );

        // Mock 설정
        doReturn(Optional.of(getMember())).when(memberRepository).findByEmail("test@test.com");
        doReturn(campaigns).when(campaignRepository).findAllByMember(any(Member.class));
        doReturn(dailySummaries).when(marginRepository).find7daysTotalsByCampaignIds(
                campaigns.stream().map(Campaign::getCampaignId).toList(),
                sevenDaysAgo,
                today
        );

        // When
        List<DailyAdSummaryDto> result = marginService.findByCampaignIdsAndDates("test@test.com", today);
        System.out.println("result = " + result);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);

        DailyAdSummaryDto summary1 = result.get(0);
        assertThat(summary1.getMarDate()).isEqualTo(today);
        assertThat(summary1.getMarAdCost()).isEqualTo(200.0);
        assertThat(summary1.getMarSales()).isEqualTo(200.0);
        assertThat(summary1.getMarRoas()).isEqualTo(1.0);

        DailyAdSummaryDto summary2 = result.get(1);
        assertThat(summary2.getMarDate()).isEqualTo(sevenDaysAgo);
        assertThat(summary2.getMarAdCost()).isEqualTo(180.0);
        assertThat(summary2.getMarSales()).isEqualTo(180.0);
        assertThat(summary2.getMarRoas()).isEqualTo(1.0);
    }
    @Test
    @DisplayName("findByCampaignIdsAndDates(): Null values handled gracefully")
    void testFindByCampaignIdsAndDates_NullValues() {
        // Given
        LocalDate today = LocalDate.of(2024, 11, 11);
        LocalDate sevenDaysAgo = today.minusDays(7);

        List<Campaign> campaigns = List.of(
                Campaign.builder().campaignId(1L).camCampaignName("Campaign 1").build()
        );

        // Mock된 DailyAdSummaryDto 생성
        List<DailyAdSummaryDto> summaryDtos = List.of(
                new DailyAdSummaryDto(today, 0.0, 0.0, 0.0),
                new DailyAdSummaryDto(sevenDaysAgo, 0.0, 0.0, 0.0)
        );

        // Mock 설정
        doReturn(Optional.of(getMember())).when(memberRepository).findByEmail("test@test.com");
        doReturn(campaigns).when(campaignRepository).findAllByMember(any(Member.class));
        doReturn(summaryDtos).when(marginRepository).find7daysTotalsByCampaignIds(
                campaigns.stream().map(Campaign::getCampaignId).toList(),
                sevenDaysAgo, today
        );

        // When
        List<DailyAdSummaryDto> result = marginService.findByCampaignIdsAndDates("test@test.com", today);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);

        // 첫 번째 DTO 검증
        DailyAdSummaryDto summary1 = result.get(0);
        assertThat(summary1.getMarDate()).isEqualTo(today);
        assertThat(summary1.getMarAdCost()).isEqualTo(0.0); // Null 처리로 기본값 확인
        assertThat(summary1.getMarSales()).isEqualTo(0.0); // Null 처리로 기본값 확인
        assertThat(summary1.getMarRoas()).isEqualTo(0.0); // ROAS 기본값 확인

        // 두 번째 DTO 검증
        DailyAdSummaryDto summary2 = result.get(1);
        assertThat(summary2.getMarDate()).isEqualTo(sevenDaysAgo);
        assertThat(summary2.getMarAdCost()).isEqualTo(0.0);
        assertThat(summary2.getMarSales()).isEqualTo(0.0);
        assertThat(summary2.getMarRoas()).isEqualTo(0.0);
    }


    private Margin newMargin(LocalDate date, Campaign campaign,Double marsale) {
        return Margin.builder()
                .marDate(date)
                .campaign(campaign)
                .marSales(marsale)
                .build();
    }
    public Member getMember() {
        return Member.builder()
                .email("test@test.com")
                .build();
    }
    // Margin 리스트 생성

}