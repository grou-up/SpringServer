package growup.spring.springserver.margin;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.margin.domain.Margin;
import growup.spring.springserver.margin.dto.MarginSummaryResponseDto;

import java.time.LocalDate;

public class TypeChangeMargin {

    /**
     *
     * @param campaign        해당 캠페인
     * @param todayMargin     오늘의 Margin 데이터
     * @param yesterdayMargin 어제의 Margin 데이터
     * @return MarginSummaryResponseDto로 변환된 결과
     */
    public static MarginSummaryResponseDto entityToMarginSummaryResponseDto(
            Campaign campaign,
            Margin todayMargin,
            Margin yesterdayMargin) {

        // 오늘과 어제의 매출 계산
        double todaySales = todayMargin.getMarSales() != null ? todayMargin.getMarSales() : 0.0;
        double yesterdaySales = yesterdayMargin.getMarSales() != null ? yesterdayMargin.getMarSales() : 0.0;
        double differentSales = todaySales - yesterdaySales;

        // DTO 생성 및 반환
        return MarginSummaryResponseDto.builder()
                .date(todayMargin.getMarDate()) // 날짜
                .campaignId(campaign.getCampaignId()) // 캠페인 ID
                .campaignName(campaign.getCamCampaignName()) // 캠페인 이름
                .todaySales(todaySales) // 오늘 매출
                .yesterdaySales(yesterdaySales) // 어제 매출
                .differentSales(differentSales) // 매출 차이
                .build();
    }

    public static Margin createDefaultMargin(Campaign campaign, LocalDate date) {
        return Margin.builder()
                .campaign(campaign)
                .marDate(date)
                .marSales(0.0) // 기본값 설정
                .build();
    }
}