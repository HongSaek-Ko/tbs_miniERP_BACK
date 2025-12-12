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
    // 품번 (엑셀/테이블 상에서 관리번호로 사용)
    private String assetId;

    // 종류
    private String assetType;

    // 제조사
    private String assetManufacturer;

    // 모델명
    private String assetModelName;

    // 시리얼번호(S/N)
    private String assetSn;

    // 제조년월 ( ... → 프론트에서 yyyy-MM 포맷 필요)
    private LocalDateTime assetManufacturedAt;

    // 성명
    private String empName;

    // 직위
    private String empPos;

    // 소속팀
    private String teamName;

    // 설치 장소
    private String assetLoc;

    // 지급일
    private LocalDateTime assetIssuanceDate;

    // 비고
    private String assetDesc;
}
