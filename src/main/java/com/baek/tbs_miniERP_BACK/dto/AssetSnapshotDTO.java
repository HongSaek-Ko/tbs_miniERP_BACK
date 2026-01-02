package com.baek.tbs_miniERP_BACK.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetSnapshotDTO {
    private String assetId;
    private String empId;
    private String holdEmp;
    private String assetStatus;
}
