package com.baek.tbs_miniERP_BACK.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AssetFilterParams {
    // 엑셀 내보내기 - 자산 목록
    private String assetType;
    private String empName;
    private String empPos;
    private String teamName;
    private String assetLoc;
    private String assetDesc;
    private String globalSearch;
    private String assetStatus;
}
