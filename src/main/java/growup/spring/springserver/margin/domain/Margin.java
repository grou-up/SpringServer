package growup.spring.springserver.margin.domain;

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
@ToString
public class Margin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 기본 키

    // 엑셀 데이터
    private LocalDate marDate;  // 날짜
    private Long marImpressions;  // 노출 수
    private Long marClicks;  // 클릭 수 -> 클릭률 구해야함
    private Long marAdConversionSales;  // 광고 전환 판매 수
    private Long marAdConversionSalesCount ; // 광고 전환 주문수 (CVR 계산 용)
    private Double marAdCost;
    private Double marSales;
    // 계산필요
    private Long marAdMargin;  // 광고 머진 계산필요
    private Double marNetProfit;  // 순이익
//    private Double marPerPiece; //  1개당 마진
//    private Double marZeroRoas; // 제로 Roas

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

    public void update(long actualSales, long adMargin) {
        this.marAdMargin = adMargin; // 광고 마진

        if (this.marActualSales == 0 && this.getMarAdMargin() == 0) {
            this.marNetProfit = 0.0;
        }else{
            this.marNetProfit = adMargin - (this.marAdCost * 1.1) ; // 순 이익 = 광고마진 - 집행광고비1.1
        }
        this.marActualSales = actualSales; // 실제 판매수
    }

    public void update(long adMargin) {
        this.marAdMargin = adMargin;
        if (this.marActualSales != 0 && this.getMarAdMargin() != 0) {
            this.marNetProfit = adMargin - (this.marAdCost * 1.1);
        }
    }
}