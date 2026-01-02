package com.baek.tbs_miniERP_BACK.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class AssetHistoryListDTO {
    private Long assetHistorySeq; // PK
    private String displayId; // "B009_H001" (조회 시 계산)
    private String assetId; // 원본 자산의 assetId

    private String assetHoldEmp; // 현재 소유자
    private String assetHoldEmpHis; // 이전 소유자
    private String assetHistoryDesc;
    private LocalDateTime assetHistoryDate;

    // UI 색상 플래그
    private Integer isFirst;// 1/0
    private Integer isTransfer;// 1/0
    private Integer isDispose;// 1/0
}
