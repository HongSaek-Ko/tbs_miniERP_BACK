package com.baek.tbs_miniERP_BACK.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetHisFilterParams {
    private String assetId;
    private String displayId; // 이력 번호
    private String assetHoldEmp; // 소유자
    private String assetHistoryDesc; // 변동 사유
    private String assetHistoryDate;
    private String globalSearch;
}
