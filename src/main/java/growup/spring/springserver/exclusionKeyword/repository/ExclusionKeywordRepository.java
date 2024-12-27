package growup.spring.springserver.exclusionKeyword.repository;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.exclusionKeyword.domain.ExclusionKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExclusionKeywordRepository extends JpaRepository<ExclusionKeyword,Long> {
    List<ExclusionKeyword> findAllByCampaign_campaignId(Long campaignId);
    Boolean existsByExclusionKeyword(String keyword);
}
