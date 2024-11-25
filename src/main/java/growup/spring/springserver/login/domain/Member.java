package growup.spring.springserver.login.domain;

import growup.spring.springserver.record.domain.Record;
import growup.spring.springserver.global.support.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@AllArgsConstructor
public class Member {
    @Id
    private String email;
    private String password;
    private String name;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 남은 멤버십 기간
    @CreatedDate
    private LocalDateTime remainMembershipTime;

    // 구매 금액에 따른 role 부여
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")  // 외래 키를 설정합니다.
    private List<Record> records;
}