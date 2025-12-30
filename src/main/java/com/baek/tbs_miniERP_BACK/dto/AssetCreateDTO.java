package com.baek.tbs_miniERP_BACK.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AssetCreateDTO {
    private String assetId;
    @NotBlank
    private String assetType; // 모니터 | 노트북
    @NotBlank
    private String assetManufacturer;
    @NotBlank
    private String assetManufacturedAt;
    @NotBlank
    private String assetModelName;
    @NotBlank
    private String assetSn;
    @NotBlank
    private String empId;
    @NotBlank
    private String assetLoc;
    @NotBlank
    private String assetIssuanceDate; // yyyy-MM-dd
    @NotBlank
    private String assetDesc;
    private LocalDateTime assetRegDt = LocalDateTime.now();
    private String assetStatus = "HOLD";

}
