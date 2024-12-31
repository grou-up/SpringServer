package growup.spring.springserver.keyword.repository;

import growup.spring.springserver.keyword.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository <Keyword,Long>{

    //TODO: 쿼리 순서 check!
    @Query("SELECT k FROM Keyword k WHERE k.keyDate BETWEEN :startDate AND :endDate " +
            "AND k.campaign.campaignId = :campaignId")
    List<Keyword> findAllByDateANDCampaign(@Param("startDate") LocalDate startDate,
                                           @Param("endDate")LocalDate endDate,
                                           @Param("campaignId")Long campaignId);
}
