package growup.spring.springserver.marginforcampaign.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MarginForCampaignResDto {
    private Long id;
    private String mfcProductName; // 상품명
    private Long mfcSalePrice; // 판매가
    private Long mfcTotalPrice; // 총비용
    private Long mfcCostPrice; // 원가
    private Long mfcPerPiece; //  1개당 마진
    private Double mfcZeroRoas; // 제로 Roas
}