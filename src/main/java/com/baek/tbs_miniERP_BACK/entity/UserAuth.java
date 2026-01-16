package com.baek.tbs_miniERP_BACK.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_AUTH")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuth {

    @EmbeddedId
    private UserAuthId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @MapsId("authCode")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTH_CODE", nullable = false)
    private Auth auth;

    // 확장 필드 예시
    @Column(name = "GRANTED_AT")
    private LocalDateTime grantedAt;

    @PrePersist
    void prePersist() {
        if (grantedAt == null) grantedAt = LocalDateTime.now();
    }
}
