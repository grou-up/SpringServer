package growup.spring.springserver.tempdomain;

import growup.spring.springserver.campaign.domain.Campaign;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@AllArgsConstructor
public class Margin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 기본 키

    private LocalDate marDate;  // 날짜
    private Double marTargetEfficiency;  // 목표 효율성
    private Double marAdRevenue;  // 광고 수익
    private Double marAdBudget;  // 광고 예산
    private Double marAdCostRatio;  // 광고비 비율
    private Double marCpcUnitCost;  // CPC 단가
    private Long marImpressions;  // 노출 수
    private Long marClicks;  // 클릭 수
    private Long marAdConversionSales;  // 광고 전환 판매 수
    private Long marActualSales;  // 실제 판매 수
    private Long marAdMargin;  // 광고 머진
    private Double marNetProfit;  // 순이익

    @ManyToOne
    @JoinColumn(name = "campaignId", referencedColumnName = "campaignId")
    private Campaign campaign;
}