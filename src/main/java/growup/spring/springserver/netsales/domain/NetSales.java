package growup.spring.springserver.netsales.domain;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.login.domain.Member;
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
public class NetSales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String netProductName;
    private Long netSalesAmount; // 순 판매금액
    private Long netSalesCount; // 순 판매수
    private LocalDate netDate;
    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email")
    private Member member;

}