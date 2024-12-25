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
        keyCpc += keyword.getKeyCpc();
        keyAdcost += keyword.getKeyAdcost();
        keyAdsales += keyword.getKeyAdsales();
        if(keyAdsales !=0 ) keyRoas = (double) (Math.round((keyAdsales/keyAdcost)*10000)) / 100.0;
        if(keyImpressions != 0 )  keyClickRate = (double) ((keyClicks/keyImpressions) * 100);
        if(keyClicks !=0 ) keyCvr = (double) ((keyTotalSales/keyClicks)*100);
    }
}
