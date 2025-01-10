package growup.spring.springserver.margin.repository;

import growup.spring.springserver.margin.domain.Margin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface MarginRepository extends JpaRepository<Margin, Long> {

    @Query("SELECT m FROM Margin m WHERE m.campaign.campaignId IN :campaignIds AND m.marDate IN :dates")
    List<Margin> findByCampaignIdsAndDates(@Param("campaignIds") List<Long> campaignIds, @Param("dates") List<LocalDate> dates);

}