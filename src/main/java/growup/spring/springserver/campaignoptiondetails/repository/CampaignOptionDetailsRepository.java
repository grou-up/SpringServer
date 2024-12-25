package growup.spring.springserver.campaignoptiondetails.repository;

import growup.spring.springserver.campaignoptiondetails.domain.CampaignOptionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignOptionDetailsRepository extends JpaRepository<CampaignOptionDetails, Long> {

    // 특정 캠페인 ID 와 날짜 범위로 detail 조회
    @Query("SELECT cod FROM CampaignOptionDetails cod " +
            "WHERE cod.execution.campaign.campaignId = :campaignId " +
            "AND cod.copDate BETWEEN :startDate AND :endDate")
    Optional<List<CampaignOptionDetails>> findByCampaignIdAndDateRange(
            @Param("campaignId") Long campaignId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}