package growup.spring.springserver.record.repository;


import growup.spring.springserver.record.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;



public interface RecordRepository extends JpaRepository<Record, String> {
}