package growup.spring.springserver.keyword.domain;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import growup.spring.springserver.campaign.domain.Campaign;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Map;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@AllArgsConstructor
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyKeyword;  // 키워드

    private Long keyImpressions;  // 노출수

    private Long keyClicks;  // 클릭수

    private Double keyClickRate;  // 클릭률

    private Long keyTotalSales;  // 총 주문수

    private Double keyCvr;  // 전환율

    private Double keyCpc;  // CPC

    private Double keyAdcost;  // 광고비

    private Double keyAdsales;  // 광고매출

    private Double keyRoas;  // ROAS

    private LocalDate keyDate;  // 날짜

    private Boolean keyExcludeFlag = false;  // 제외여부

    private String keySearchType;  // 검색 비검색

    @Type(JsonBinaryType.class) // JSON 필드 매핑
    @Column(columnDefinition = "json")
    private Map<String, Long> keyProductSales; // JSON 형태로 상품ID와 판매량 저장

    @ManyToOne
    @JoinColumn(name = "campaignId", referencedColumnName = "campaignId")
    private Campaign campaign;
}
