package growup.spring.springserver.netsales.repository;

import growup.spring.springserver.netsales.domain.NetSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface NetRepository extends JpaRepository<NetSales, Long> {

    @Query("SELECT ns FROM NetSales ns WHERE ns.netDate = :netDate AND ns.member.email = :email AND ns.netProductName = :netProductName")
    Optional<NetSales> findByNetDateAndEmailAndNetProductName(@Param("netDate") LocalDate netDate,
                                                             @Param("email") String email,
                                                             @Param("netProductName") String netProductName);
}