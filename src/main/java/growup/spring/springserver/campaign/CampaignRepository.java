package growup.spring.springserver.campaign;

import growup.spring.springserver.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign,Long> {

    Optional<Campaign> findByCampaignId(Long Long);

    List<Campaign> findAllByMember(Member member);

}