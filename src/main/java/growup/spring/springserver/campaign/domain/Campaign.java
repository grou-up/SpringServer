package growup.spring.springserver.campaign.domain;

import growup.spring.springserver.login.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@AllArgsConstructor
public class Campaign {

    @Id
    private Long campaignId;

    private String camCampaignName;

    private String camAdType;

    private Boolean camOpen = false;

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email")
    private Member member;

}