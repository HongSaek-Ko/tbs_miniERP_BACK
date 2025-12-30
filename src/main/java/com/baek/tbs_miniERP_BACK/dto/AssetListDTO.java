package com.baek.tbs_miniERP_BACK.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetListDTO {
    private String assetId; // 품번
    private String assetType; // 종류
    private String assetManufacturer; // 제조사
    private String assetModelName; // 모델명
    private String assetSn; // 시리얼번호
    private LocalDateTime assetManufacturedAt; // 제조일자
    private String empName; // 사원명
    private String empPos; // 직책
    private String teamName; // 소속(팀 이름)
    private String assetLoc; // 설치 장소
    private LocalDateTime assetIssuanceDate; // 지급일자
    private String assetDesc; // 비고
}
