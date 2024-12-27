package growup.spring.springserver.execution.repository;

import growup.spring.springserver.execution.domain.Execution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution,Long> {

    @Query("SELECT e.executionId FROM Execution e WHERE e.campaign.campaignId = :campaignId")
    List<Long> findExecutionIdsByCampaignId(Long campaignId);
}