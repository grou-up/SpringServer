package growup.spring.springserver.marginforcampaign.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MfcDto {
    private String mfcProductName; // 상품명
    private Long mfcSalePrice;
    private Long mfcTotalPrice; // 총비용
    private Long mfcCostPrice; // 원가
    private Double mfcPerPiece; //  1개당 마진
    private Double mfcZeroRoas; // 제로 Roas
}