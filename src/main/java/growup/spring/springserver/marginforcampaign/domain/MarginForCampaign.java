package growup.spring.springserver.marginforcampaign.domain;

import growup.spring.springserver.campaign.domain.Campaign;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@AllArgsConstructor
public class MarginForCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mfc_id")
    private Long id;

    private String mfcProductName; // 상품명
    private Long mfcTotalPrice; // 총비용
    private Long mfcCostPrice; // 원가
    private Double mfcPerPiece; //  1개당 마진
    private Double mfcZeroRoas; // 제로 Roas

    @ManyToOne
    @JoinColumn(name = "campaignId", referencedColumnName = "campaignId")
    private Campaign campaign;
}