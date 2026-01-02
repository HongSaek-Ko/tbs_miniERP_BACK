package com.baek.tbs_miniERP_BACK.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AssetHisCreDTO {
    private String assetId;
    private String holdEmpHis; // 이전 소유자
    private String holdEmp; // 현재 소유지
    private String historyDesc; // 비고(변동 사유)
//    private LocalDateTime historyDate; // 변동 일시
}
