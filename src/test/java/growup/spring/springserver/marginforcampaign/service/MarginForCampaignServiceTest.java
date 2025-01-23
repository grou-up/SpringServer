package growup.spring.springserver.marginforcampaign.service;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.marginforcampaign.domain.MarginForCampaign;
import growup.spring.springserver.marginforcampaign.dto.MarginForCampaignResDto;
import growup.spring.springserver.marginforcampaign.repository.MarginForCampaignRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class MarginForCampaignServiceTest {
    @Mock
    private MarginForCampaignRepository marginForCampaignRepository;

    @InjectMocks
    private MarginForCampaignService marginForCampaignService;

    @Test
    @DisplayName("get MarginForCampaignByCampaignId Success 1. Empty")
    void MarginForCampaignByCampaignId_Success1() {
        doReturn(List.of()).when(marginForCampaignRepository).MarginForCampaignByCampaignId(1L);

        final List<MarginForCampaignResDto> result = marginForCampaignService.marginForCampaignByCampaignId(1L);
        assertThat(result).hasSize(0);
    }

    @Test
    @DisplayName("get MarginForCampaignByCampaignId Success 2. ")
    void MarginForCampaignByCampaignId_Success2() {
        //given
        Campaign campaign = getCampaign("송보석", 1L);
        doReturn(List.of(
                getgetMarginForCampaign(campaign, "모자1", 1L, 1L, 1.1, 1.1),
                getgetMarginForCampaign(campaign, "모자2", 1L, 1L, 1.1, 1.1),
                getgetMarginForCampaign(campaign, "모자3", 1L, 1L, 1.1, 1.1)))
                .when(marginForCampaignRepository).MarginForCampaignByCampaignId(1L);
        //when

        final List<MarginForCampaignResDto> result = marginForCampaignService.marginForCampaignByCampaignId(1L);

        // given
        assertThat(result).hasSize(3);


    }

    public Campaign getCampaign(String name, Long id) {
        return Campaign.builder()
                .campaignId(id)
                .camCampaignName(name)
                .build();
    }

    public MarginForCampaign getgetMarginForCampaign(Campaign campaign, String productName, Long mfcTotalPrice, Long mfcCostPrice, Double mfcPerPiece, Double mfcZeroRoas) {
        return MarginForCampaign.builder()
                .mfcProductName(productName)
                .mfcTotalPrice(mfcTotalPrice)
                .mfcCostPrice(mfcCostPrice)
                .mfcPerPiece(mfcPerPiece)
                .mfcZeroRoas(mfcZeroRoas)
                .campaign(campaign)
                .build();
    }
}