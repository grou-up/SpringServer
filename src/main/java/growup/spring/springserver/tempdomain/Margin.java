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

    // 엑셀 데이터
    private LocalDate marDate;  // 날짜
    private Long marImpressions;  // 노출 수
    private Long marClicks;  // 클릭 수 -> 클릭률 구해야함
    private Long marAdConversionSales;  // 광고 전환 판매 수
    private Double marAdCost;
    // 계산필요
    private Long marAdMargin;  // 광고 머진 계산필요
    private Double marNetProfit;  // 순이익

    // 입력 받아야 함
    private Double marTargetEfficiency;  // 목표 효율성
    private Double marAdBudget;  // 광고 예산


    //    statics 엑셀파일 필요
    private Long marActualSales;  // 실제 판매 수

    //    위에 컬럼들로 계싼할 수 있음
//    private Double marAdRevenue;  // 광고 수익률
//    private Double marCpcUnitCost;  // CPC 단가 계산
//    집행광고비 : 위에 있는 광고비 * 1.1
//    private Double marCvr // 전환율



    @ManyToOne
    @JoinColumn(name = "campaignId", referencedColumnName = "campaignId")
    private Campaign campaign;
}