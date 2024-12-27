package growup.spring.springserver.exclusionKeyword.repository;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.exclusionKeyword.domain.ExclusionKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExclusionKeywordRepository extends JpaRepository<ExclusionKeyword,Long> {
    List<ExclusionKeyword> findAllByCampaign_campaignId(Long campaignId);
    Boolean existsByExclusionKeyword(String keyword);


    @Modifying
    @Query("DELETE FROM ExclusionKeyword k WHERE k.campaign.campaignId = :campaignId AND k.exclusionKeyword = :exclusionKeyword")
    int deleteByCampaign_campaignIdANDExclusionKeyword(@Param("campaignId") Long campaignId,
                                                        @Param("exclusionKeyword") String exclusionKeyword);
}
