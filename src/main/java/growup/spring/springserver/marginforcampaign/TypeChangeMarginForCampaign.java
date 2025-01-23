package growup.spring.springserver.marginforcampaign;


import growup.spring.springserver.marginforcampaign.domain.MarginForCampaign;
import growup.spring.springserver.marginforcampaign.dto.MarginForCampaignResDto;

public class TypeChangeMarginForCampaign {

    public static MarginForCampaignResDto EntityToDto(MarginForCampaign data) {
        return MarginForCampaignResDto.builder()
                .mfcProductName(data.getMfcProductName())
                .mfcTotalPrice(data.getMfcTotalPrice())
                .mfcCostPrice(data.getMfcCostPrice())
                .mfcPerPiece(data.getMfcPerPiece())
                .mfcZeroRoas(data.getMfcZeroRoas())
                .build();
    }
}