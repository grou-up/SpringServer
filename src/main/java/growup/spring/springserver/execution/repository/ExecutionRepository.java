package growup.spring.springserver.execution.repository;

import growup.spring.springserver.execution.domain.Execution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution,Long> {
}