package com.baek.tbs_miniERP_BACK.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ASSET_HISTORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetHistory {

    @Id
    @Column(name = "ASSET_HISTORY_ID", length = 30)
    private String assetHistoryId; // 예: B009_H001 (트리거가 채움)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSET_ID", nullable = false)
    private Asset asset;

    @Column(name = "ASSET_HOLD_EMP_HIS", length = 100)
    private String assetHoldEmpHis; // 이전 소유자 (없으면 NULL)

    @Column(name = "ASSET_HOLD_EMP", length = 100, nullable = false)
    private String assetHoldEmp; // 현재 소유자 (예: 홍길동 (E001))

    @Column(name = "ASSET_HISTORY_DESC", length = 200)
    private String assetHistoryDesc;

    @CreationTimestamp
    @Column(name = "ASSET_HISTORY_DATE", nullable = false)
    private LocalDateTime assetHistoryDate;

    @Column(name = "ASSET_STATUS_AT_TIME", length = 10, nullable = false)
    private String assetStatusAtTime; // HOLD / N
}
