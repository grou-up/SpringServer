package growup.spring.springserver.marginforcampaign.repository;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.repository.CampaignRepository;
import growup.spring.springserver.marginforcampaign.domain.MarginForCampaign;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MarginForCampaignRepositoryTest {
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private MarginForCampaignRepository marginForCampaignRepository;

    @DisplayName("save success")
    @Test
    void test1() {
        final MarginForCampaign marginForCampaign = getgetMarginForCampaign(getCampaign("송보석",1L), "모자", 1L, 1L, 1.1, 1.1);
        MarginForCampaign result = marginForCampaignRepository.save(marginForCampaign);

        assertThat(result.getCampaign().getCamCampaignName()).isEqualTo("송보석");
    }

    @DisplayName("MarginForCampaignByCampaignId ")
    @Test
    void test2() {
        Campaign campaign = campaignRepository.save(getCampaign("송보석",1L));
        Campaign IncorrectCampaign = campaignRepository.save(getCampaign("Not송보석",2L));
//        given
        marginForCampaignRepository.save(getgetMarginForCampaign(campaign, "모자", 1L, 1L, 1.1, 1.1));
        marginForCampaignRepository.save(getgetMarginForCampaign(campaign, "모자2", 1L, 1L, 1.1, 1.1));
        marginForCampaignRepository.save(getgetMarginForCampaign(campaign, "모자3", 1L, 1L, 1.1, 1.1));

        marginForCampaignRepository.save(getgetMarginForCampaign(IncorrectCampaign, "모자3", 1L, 1L, 1.1, 1.1));

//        when
        List<MarginForCampaign> result = marginForCampaignRepository.MarginForCampaignByCampaignId(1L);
//        then
        assertThat(result).hasSize(3);
        for (MarginForCampaign data : result) {
            assertThat(data.getCampaign().getCamCampaignName()).isEqualTo("송보석");
        }
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