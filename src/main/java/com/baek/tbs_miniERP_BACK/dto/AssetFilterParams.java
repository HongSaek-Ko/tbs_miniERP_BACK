package com.baek.tbs_miniERP_BACK.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetFilterParams {
    private String assetType;
    private String empName;
    private String empPos;
    private String teamName;
    private String assetLoc;
    private String assetDesc;
    private String globalSearch;
    private String assetStatus;
}
