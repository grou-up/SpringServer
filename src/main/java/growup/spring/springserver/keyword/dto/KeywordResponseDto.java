package growup.spring.springserver.keyword.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class KeywordResponseDto {

    private String keyKeyword;  // 키워드

    private Long keyImpressions;  // 노출수

    private Long keyClicks;  // 클릭수

    private Double keyClickRate;  // 클릭률

    private Long keyTotalSales;  // 총 주문수

    private Double keyCvr;  // 전환율

    private Double keyCpc;  // CPC

    private Double keyAdcost;  // 광고비

    private Double keyAdsales;  // 광고매출

    private Double keyRoas;  // ROAS

    private LocalDate keyDate;  // 날짜

    private Boolean keyExcludeFlag = false;  // 제외여부

    private String keySearchType;  // 검색 비검색
}
