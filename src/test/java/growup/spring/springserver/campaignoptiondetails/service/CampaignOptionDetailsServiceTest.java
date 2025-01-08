package growup.spring.springserver.campaignoptiondetails.service;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.dto.CampaignResponseDto;
import growup.spring.springserver.campaign.repository.CampaignRepository;
import growup.spring.springserver.campaignoptiondetails.domain.CampaignOptionDetails;
import growup.spring.springserver.campaignoptiondetails.dto.CampaignOptionDetailsResponseDto;
import growup.spring.springserver.campaignoptiondetails.dto.CampaignSummaryResponseDto;
import growup.spring.springserver.campaignoptiondetails.repository.CampaignOptionDetailsRepository;
import growup.spring.springserver.exception.InvalidDateFormatException;
import growup.spring.springserver.exception.campaign.CampaignNotFoundException;
import growup.spring.springserver.exception.campaignoptiondetails.CampaignOptionDataNotFoundException;
import growup.spring.springserver.exception.campaignoptiondetails.CampaignOptionNotFoundException;
import growup.spring.springserver.execution.domain.Execution;
import growup.spring.springserver.execution.repository.ExecutionRepository;
import growup.spring.springserver.login.domain.Member;
import growup.spring.springserver.login.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CampaignOptionDetailsServiceTest {

    @Mock
    private CampaignOptionDetailsRepository campaignOptionDetailsRepository;

    @Mock
    private ExecutionRepository executionRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private CampaignOptionDetailsService campaignOptionDetailsService;

    @Test
    @DisplayName("getCampaignDetails : Error 1. 해당 켐페인의 옵션이 없는경우")
    void test1() {
        // Given
        doReturn(List.of())
                .when(executionRepository)
                .findExecutionIdsByCampaignId(any());

        // When & Then
        CampaignOptionNotFoundException exception = assertThrows(CampaignOptionNotFoundException.class,
                () -> campaignOptionDetailsService.getCampaignDetailsByCampaignsIds(
                        LocalDate.of(2024, 12, 25),
                        LocalDate.of(2024, 12, 25),
                        1L));

        // Then
        assertThat(exception.getMessage()).isEqualTo("해당 캠페인의 옵션이 없습니다.");
    }

    @Test
    @DisplayName("getCampaignDetails : Error 2. 날짜 오류")
    void test2() {
        // given
        final LocalDate start = LocalDate.of(2024, 11, 11);
        final LocalDate end = LocalDate.of(2025, 12, 13);

        // when
        final Exception result = assertThrows(InvalidDateFormatException.class,
                () -> campaignOptionDetailsService.getCampaignDetailsByCampaignsIds(end, start, 1L));
        //then
        assertThat(result.getMessage()).isEqualTo("날짜 형식이 이상합니다.");
    }

    @Test
    @DisplayName("getCampaignDetails : Error 3. 해당 옵션의 데이터가 없음")
    void test3() {
        // given
        final LocalDate start = LocalDate.of(2024, 11, 11);
        final LocalDate end = LocalDate.of(2024, 11, 13);

        List<Execution> executions = List.of(
                newExecution(1L, 1L, "상품1", "상품1"),
                newExecution(2L, 2L, "상품2", "상품2"),
                newExecution(3L, 3L, "상품3", "상품3")
        );

        doReturn(executions).when(executionRepository).findExecutionIdsByCampaignId(any());

        // when
        doReturn(List.of())
                .when(campaignOptionDetailsRepository)
                .findByExecutionIdsAndDateRange(anyList(), any(LocalDate.class), any(LocalDate.class));

        // then
        final CampaignOptionDataNotFoundException result = assertThrows(CampaignOptionDataNotFoundException.class,
                () -> campaignOptionDetailsService.getRawCampaignDetails(start, end, 1L));

        assertThat(result.getMessage()).isEqualTo("해당 옵션의 데이터가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("getCampaignDetails : Success 1. 기간에 맞으면서 raw 하게 execution 에 맞는 데이터들 가져오기.")
    void test4() {
        // given
        final LocalDate start = LocalDate.of(2024, 11, 11);
        final LocalDate end = LocalDate.of(2024, 11, 13);

        List<Long> executionIds = List.of(1L, 2L, 3L);
        doReturn(executionIds).when(executionRepository).findExecutionIdsByCampaignId(any());

        Execution execution1 = newExecution(1L, 1L, "Product A", "Category A");
        Execution execution2 = newExecution(2L, 2L, "Product B", "Category B");
        Execution execution3 = newExecution(3L, 3L, "Product C", "Category C");
        System.out.println("execution3 = " + execution1.toString());
        // Mock 데이터: CampaignOptionDetails
        List<CampaignOptionDetails> campaignOptionDetailsList = List.of(
                newCampaignOptionDetails(1L, start, 100L, 50.0, 200.0, 400.0, 2L, 0.5, 2.5, "검색", 3L, execution1),
                newCampaignOptionDetails(2L, end, 200L, 30.0, 150.0, 300.0, 3L, 0.8, 3.0, "비검색", 3L, execution2),
                newCampaignOptionDetails(3L, end.plusDays(1), 200L, 30.0, 150.0, 300.0, 3L, 0.8, 3.0, "비검색", 3L, execution3), // 속하지 않는 데이터
                newCampaignOptionDetails(4L, end, 200L, 30.0, 150.0, 300.0, 3L, 0.8, 3.0, "비검색", 3L, execution3)
        );
        // Mock 설정: 날짜 범위를 기반으로 데이터를 필터링
        doAnswer(data -> {
            List<Long> ids = data.getArgument(0);
            LocalDate from = data.getArgument(1);
            LocalDate to = data.getArgument(2);
            return campaignOptionDetailsList.stream()
                    .filter(detail -> ids.contains(detail.getExecution().getId()) &&
                            (detail.getCopDate().isEqual(from) || detail.getCopDate().isAfter(from)) &&
                            (detail.getCopDate().isEqual(to) || detail.getCopDate().isBefore(to)))
                    .toList();
        }).when(campaignOptionDetailsRepository)
                .findByExecutionIdsAndDateRange(anyList(), any(LocalDate.class), any(LocalDate.class));

        // when
        final List<CampaignOptionDetails> result = campaignOptionDetailsService.getRawCampaignDetails(start, end, 1L);

        System.out.println("result = " + Arrays.toString(result.toArray()));

        // then
        assertThat(result).hasSize(3); // 날짜 범위에 맞는 3개의 데이터가 반환되어야 함

    }

    @Test
    @DisplayName("getCampaignDetails : Success 2. 데이터 계산")
    void test5() {
        // Given
        final LocalDate start = LocalDate.of(2024, 11, 11);
        final LocalDate end = LocalDate.of(2024, 11, 13);

        List<Long> executionIds = List.of(1L, 2L, 3L);
        doReturn(executionIds).when(executionRepository).findExecutionIdsByCampaignId(any());

        Execution execution1 = newExecution(1L, 1L, "Product A", "Category A");
        Execution execution2 = newExecution(2L, 2L, "Product B", "Category B");

        // Mock 데이터 생성
        List<CampaignOptionDetails> campaignOptionDetailsList = List.of(
                newCampaignOptionDetails(1L, start, 1L, 10.0, 10.0, 10.0, 1L, 0.3, 2.5, "검색", 3L, execution1),
                newCampaignOptionDetails(2L, end, 3L, 500.0, 5000.0, 10.0, 3L, 0.5, 3.0, "비검색", 1L, execution2),
                newCampaignOptionDetails(3L, end, 3L, 500.0, 5000.0, 10.0, 1L, 0.5, 3.0, "비검색", 1L, execution2)
        );
        System.out.println("campaignOptionDetailsList = " + campaignOptionDetailsList.toString());
        doReturn(campaignOptionDetailsList).when(campaignOptionDetailsRepository)
                .findByExecutionIdsAndDateRange(anyList(), eq(start), eq(end));

        // When
        List<CampaignOptionDetailsResponseDto> result = campaignOptionDetailsService.getCampaignDetailsByCampaignsIds(start, end, 1L);
        System.out.println("result = " + result);
        // Then
        System.out.println("result = " + result.get(0).toString());
        assertThat(result.get(0).getCopImpressions()).isEqualTo(1L);
        assertThat(result.get(1).getCopImpressions()).isEqualTo(6L);
        assertThat(result).hasSize(2);

    }

    @Test
    @DisplayName("getCampaignAllSales: Error 1. 캠페인이 없는경우")
    void test6() {

        final LocalDate date = LocalDate.of(2024, 11, 11);

        //given
        doReturn(Optional.of(getMember())).when(memberRepository).findByEmail(any(String.class));
        doReturn(new ArrayList<Campaign>()).when(campaignRepository).findAllByMember(any(Member.class));
        //when
        final Exception result = assertThrows(CampaignNotFoundException.class,
                () -> campaignOptionDetailsService.getCampaignAllSales(date, "test@test.com"));
        //then
        assertThat(result.getMessage()).isEqualTo("현재 등록된 캠페인이 없습니다.");

    }

    @Test
    @DisplayName("getCampaignAllSales : 성공 케이스")
    void testGetCampaignAllSalesSuccess() {
        // Given
        LocalDate today = LocalDate.of(2024, 11, 11);
        LocalDate yesterday = today.minusDays(1);

        // Mock 캠페인 데이터
        List<Campaign> campaigns = List.of(
                Campaign.builder().camCampaignName("Campaign1").campaignId(1L).build(),
                Campaign.builder().camCampaignName("Campaign2").campaignId(2L).build(),
                Campaign.builder().camCampaignName("Campaign3").campaignId(3L).build());

        doReturn(Optional.of(getMember())).when(memberRepository).findByEmail(any(String.class));
        doReturn(campaigns).when(campaignRepository).findAllByMember(any(Member.class));

        // Mock Execution 데이터
        Execution execution1 = newExecution(1L, 1L, "Product A", "Category A");
        Execution execution2 = newExecution(2L, 2L, "Product B", "Category B");
        Execution execution3 = newExecution(3L, 3L, "Product C", "Category C");

        // Mock Execution IDs
        doReturn(List.of(1L)).when(executionRepository).findExecutionIdsByCampaignId(1L);
        doReturn(List.of(2L, 3L)).when(executionRepository).findExecutionIdsByCampaignId(2L);
        doReturn(List.of(4L)).when(executionRepository).findExecutionIdsByCampaignId(3L);

        // Mock 오늘 데이터
        List<CampaignOptionDetails> todayDetails1 = List.of(
                newCampaignOptionDetails(1L, today, 100L, 200.0, 500.0, 2.5, 50L, 10.0, 15.0, "검색", 5L, execution1),
                newCampaignOptionDetails(2L, today, 150L, 250.0, 600.0, 2.4, 60L, 12.0, 14.0, "비검색", 6L, execution1)
        );
        List<CampaignOptionDetails> todayDetails2 = List.of(
                newCampaignOptionDetails(3L, today, 200L, 300.0, 700.0, 2.33, 70L, 11.5, 16.0, "검색", 7L, execution2),
                newCampaignOptionDetails(4L, today, 250L, 350.0, 800.0, 2.28, 80L, 10.0, 15.0, "비검색", 8L, execution2),
                newCampaignOptionDetails(5L, today, 300L, 400.0, 900.0, 2.25, 90L, 10.0, 13.0, "검색", 9L, execution2)
        );
        List<CampaignOptionDetails> todayDetails3 = List.of(
                newCampaignOptionDetails(6L, today, 400L, 500.0, 1000.0, 2.0, 100L, 9.0, 12.0, "검색", 10L, execution3)
        );

        // Mock 어제 데이터
        List<CampaignOptionDetails> yesterdayDetails1 = List.of(
                newCampaignOptionDetails(7L, yesterday, 50L, 100.0, 300.0, 3.0, 25L, 15.0, 20.0, "검색", 3L, execution1)
        );
        List<CampaignOptionDetails> yesterdayDetails2 = List.of(
                newCampaignOptionDetails(8L, yesterday, 100L, 150.0, 400.0, 2.67, 30L, 20.0, 18.0, "검색", 4L, execution2),
                newCampaignOptionDetails(9L, yesterday, 200L, 250.0, 600.0, 2.4, 40L, 25.0, 16.0, "비검색", 5L, execution2)
        );
        List<CampaignOptionDetails> yesterdayDetails3 = List.of(
                newCampaignOptionDetails(10L, yesterday, 300L, 400.0, 900.0, 2.25, 50L, 30.0, 15.0, "검색", 6L, execution3)
        );

        // Mock campaignOptionDetailsRepository 호출 설정
        doAnswer(invocation -> {
            List<Long> ids = invocation.getArgument(0);
            LocalDate from = invocation.getArgument(1);
            LocalDate to = invocation.getArgument(2);

            if (from.equals(today) && to.equals(today)) {
                if (ids.contains(1L)) return todayDetails1;
                if (ids.contains(2L) || ids.contains(3L)) return todayDetails2;
                if (ids.contains(4L)) return todayDetails3;
            } else if (from.equals(yesterday) && to.equals(yesterday)) {
                if (ids.contains(1L)) return yesterdayDetails1;
                if (ids.contains(2L) || ids.contains(3L)) return yesterdayDetails2;
                if (ids.contains(4L)) return yesterdayDetails3;
            }

            return List.of();
        }).when(campaignOptionDetailsRepository).findByExecutionIdsAndDateRange(anyList(), any(LocalDate.class), any(LocalDate.class));

        // When
        List<CampaignSummaryResponseDto> result = campaignOptionDetailsService.getCampaignAllSales(today, "test@example.com");

        // Then
        assertThat(result).hasSize(3);

        // Campaign 1
        assertThat(result.get(0).getCampaignId()).isEqualTo(1L);
        assertThat(result.get(0).getCampaignName()).isEqualTo("Campaign1");
        assertThat(result.get(0).getYesterdaySales()).isEqualTo(300.0);
        assertThat(result.get(0).getTodaySales()).isEqualTo(1100.0);
        assertThat(result.get(0).getDifferentSales()).isEqualTo(800.0);

        // Campaign 2
        assertThat(result.get(1).getCampaignId()).isEqualTo(2L);
        assertThat(result.get(1).getCampaignName()).isEqualTo("Campaign2");
        assertThat(result.get(1).getYesterdaySales()).isEqualTo(1000.0);
        assertThat(result.get(1).getTodaySales()).isEqualTo(2400.0);
        assertThat(result.get(1).getDifferentSales()).isEqualTo(1400.0);

        // Campaign 3
        assertThat(result.get(2).getCampaignId()).isEqualTo(3L);
        assertThat(result.get(2).getCampaignName()).isEqualTo("Campaign3");
        assertThat(result.get(2).getYesterdaySales()).isEqualTo(900.0);
        assertThat(result.get(2).getTodaySales()).isEqualTo(1000.0);
        assertThat(result.get(2).getDifferentSales()).isEqualTo(100.0);
    }


    public Execution newExecution(long l, long executionid, String detail, String name) {
        return Execution.builder()
                .id(l)
                .exeId(executionid)
                .exeDetailCategory(detail)
                .exeProductName(name)
                .build();
    }

    public CampaignOptionDetails newCampaignOptionDetails(
            long id, LocalDate date, Long impressions, Double copadcost, Double copadsales,
            Double coproas, Long copclicks, Double copclickRate, Double copcvr, String copsearchType,
            Long copSales, Execution execution
    ) {
        return CampaignOptionDetails.builder()
                .id(id)
                .copDate(date)
                .copImpressions(impressions)
                .copAdcost(copadcost)
                .copAdsales(copadsales)
                .copRoas(coproas)
                .copClicks(copclicks)
                .copClickRate(copclickRate)
                .copCvr(copcvr)
                .copSearchType(copsearchType)
                .copSales(copSales)
                .execution(execution)
                .build();
    }

    public Member getMember() {
        return Member.builder()
                .email("test@test.com")
                .build();
    }
}