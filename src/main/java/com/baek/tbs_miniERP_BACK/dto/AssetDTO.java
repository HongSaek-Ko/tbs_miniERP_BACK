package com.baek.tbs_miniERP_BACK.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
public class AssetDTO {
    private String assetId;
    private String assetStatus;
}
