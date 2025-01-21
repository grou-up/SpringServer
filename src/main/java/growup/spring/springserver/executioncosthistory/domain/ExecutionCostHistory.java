package growup.spring.springserver.executioncosthistory.domain;

import growup.spring.springserver.execution.domain.Execution;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@AllArgsConstructor
public class ExecutionCostHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate echStartDate;
    private LocalDate echEndDate;
    private Double echMargin;

    @ElementCollection
    private List<String> echChangedFields = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "execution_id", referencedColumnName = "execution_id")
    private Execution execution;

    // Helper 메서드: 변경된 필드를 추가
    public void addChangedField(String fieldName) {
        if (!echChangedFields.contains(fieldName)) {
            echChangedFields.add(fieldName);
        }
    }
}