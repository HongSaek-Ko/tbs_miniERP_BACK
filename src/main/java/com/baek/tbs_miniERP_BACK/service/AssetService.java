package com.baek.tbs_miniERP_BACK.service;

import com.baek.tbs_miniERP_BACK.dto.AssetListDTO;
import com.baek.tbs_miniERP_BACK.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;

    public Page<AssetListDTO> getAssetList(String assetType, String empName, String empPos, Integer page, Integer size) {
        // 페이지 번호 없거나 0보다 작으면 0, 있으면 그 값으로. 사이즈(한 페이지 당 개수)도 동일.
        int pageNo = (page == null || page < 0) ? 0 : page;
        int pageSize = (size == null || size < 0) ? 50 : size;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return assetRepository.getAssetList(assetType, empName, empPos, pageable);
    }

    // 엑셀 Export: 전체 데이터
    public List<AssetListDTO> getAssetListForExport(
            String assetType,
            String empName,
            String empPos
    ) {
        List<AssetListDTO> list =
                assetRepository.getAssetList(assetType, empName, empPos, Pageable.unpaged())
                        .getContent();
        return list;
    }
}
