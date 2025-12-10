package com.baek.tbs_miniERP_BACK.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ASSET")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "asset_seq_gen")
    @SequenceGenerator(
            name = "asset_seq_gen",
            sequenceName = "ASSET_SEQ",
            allocationSize = 1
    )
    @Column(name = "ASSET_ID")
    private Long assetId;

    @Column(name = "ASSET_TYPE", length = 10)
    private String assetType;

    @Column(name = "ASSET_MANUFACTURER", length = 100)
    private String assetManufacturer;

    @Column(name = "ASSET_MODEL_NAME", length = 100)
    private String assetModelName;

    @Column(name = "ASSET_SN", length = 100)
    private String assetSn;

    @Column(name = "ASSET_MANUFACTURED_AT")
    private LocalDateTime assetManufacturedAt;

    @Column(name = "ASSET_LOC", length = 100)
    private String assetLoc;

    @Column(name = "ASSET_ISSUANCE_DATE")
    private LocalDateTime assetIssuanceDate;

    @Column(name = "ASSET_DESC", length = 100)
    private String assetDesc;

    // HOLD, DISPOSE
    @Column(name = "ASSET_STATUS", length = 10, nullable = false)
    private String assetStatus;

    @Column(name = "ASSET_REG_DT", nullable = false)
    private LocalDateTime assetRegDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMP_ID")
    private Employee employee;

    @PrePersist
    private void onCreate() {
        if (this.assetStatus == null) {
            this.assetStatus = "HOLD";
        }
        if (this.assetRegDt == null) {
            this.assetRegDt = LocalDateTime.now();
        }
    }
}
