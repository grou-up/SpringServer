package growup.spring.springserver.execution.repository;

import growup.spring.springserver.execution.domain.Execution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution,Long> {

    @Query("SELECT e.id FROM Execution e WHERE e.campaign.campaignId = :campaignId")
    List<Long> findExecutionIdsByCampaignId(Long campaignId);

    @Query("SELECT e FROM Execution e WHERE e.campaign.campaignId = :campaignId")
    List<Execution> findExecutionByCampaignId(Long campaignId);

    Optional<Execution> findByCampaign_CampaignIdAndExeId(Long campaignId, Long executionId);

}