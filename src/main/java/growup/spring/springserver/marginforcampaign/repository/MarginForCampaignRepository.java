package growup.spring.springserver.marginforcampaign.repository;

import growup.spring.springserver.marginforcampaign.domain.MarginForCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarginForCampaignRepository extends JpaRepository<MarginForCampaign, Long> {

    @Query("SELECT m FROM MarginForCampaign m WHERE m.campaign.campaignId = :campaignId")
    List<MarginForCampaign> MarginForCampaignByCampaignId(Long campaignId);
}