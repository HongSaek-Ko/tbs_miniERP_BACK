package com.baek.tbs_miniERP_BACK.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AssetHistoryDTO {
    private String assetHistoryId;
    private String assetId;

    private String assetHoldEmp;
    private String assetHoldEmpHis;

    private String assetHistoryDesc;
    private LocalDateTime assetHistoryDate;

    private String assetStatusAtTime;
}
