package growup.spring.springserver.keyword.dto;

import growup.spring.springserver.keyword.domain.Keyword;
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

    private Double keyCvr ;  // 전환율

    private Double keyCpc;  // CPC

    private Double keyAdcost;  // 광고비

    private Double keyAdsales;  // 광고매출

    private Double keyRoas ;  // ROAS

    private LocalDate keyDate;  // 날짜

    private Boolean keyExcludeFlag = false;  // 제외여부

    private String keySearchType;// 검색 비검색

    public void update(Keyword keyword){
        keyClicks += keyword.getKeyClicks();
        keyImpressions += keyword.getKeyImpressions();
        keyTotalSales += keyword.getKeyTotalSales();
        keyAdcost += keyword.getKeyAdcost();
        keyAdsales += keyword.getKeyAdsales();
        if(keyAdcost !=0 ) keyCpc = (double) (Math.round(((keyAdcost/ (double) keyClicks)*100)))/100.0  ;
        if(keyAdsales !=0 ) keyRoas = (double) (Math.round((keyAdsales/keyAdcost)*10000)) / 100.0;
        if(keyImpressions != 0 )  keyClickRate = (double) (Math.round(((double)keyClicks/(double)keyImpressions) * 10000))/100.0;
        if(keyClicks !=0 ) keyCvr = (double) (Math.round(((double)keyTotalSales/(double)keyClicks)*10000)) /100.0;
    }
}
