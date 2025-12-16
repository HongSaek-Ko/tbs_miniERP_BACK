package com.baek.tbs_miniERP_BACK.service;

import com.baek.tbs_miniERP_BACK.dto.AssetDisposeDTO;
import com.baek.tbs_miniERP_BACK.dto.AssetFilterParams;
import com.baek.tbs_miniERP_BACK.dto.AssetListDTO;
import com.baek.tbs_miniERP_BACK.entity.Asset;
import com.baek.tbs_miniERP_BACK.entity.Employee;
import com.baek.tbs_miniERP_BACK.entity.Team;
import com.baek.tbs_miniERP_BACK.repository.AssetRepository;
import com.baek.tbs_miniERP_BACK.util.AssetSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;

    public Page<AssetListDTO> getAssetList(AssetFilterParams params, Pageable pageable) {
        Specification<Asset> spec = AssetSpecifications.byFilters(params);

        return assetRepository.findAll(spec, pageable)
                .map(this::toDto);
    }

    public List<AssetListDTO> getAssetListForExport(AssetFilterParams params) {
        Specification<Asset> spec = AssetSpecifications.byFilters(params);

        return assetRepository.findAll(spec).stream()
                .map(this::toDto)
                .toList();
    }

    private AssetListDTO toDto(Asset a) {
        Employee e = a.getEmployee();
        Team t = (e != null ? e.getTeam() : null);

        return new AssetListDTO(
                a.getAssetId(),
                a.getAssetType(),
                a.getAssetManufacturer(),
                a.getAssetModelName(),
                a.getAssetSn(),
                a.getAssetManufacturedAt(),
                e != null ? e.getEmpName() : null,
                e != null ? e.getEmpPos() : null,
                t != null ? t.getTeamName() : null,
                a.getAssetLoc(),
                a.getAssetIssuanceDate(),
                a.getAssetDesc()
        );
    }
    @Transactional
    public void disposeAssets(List<AssetDisposeDTO> reqs) {

        for (AssetDisposeDTO dto : reqs) {
            Asset asset = assetRepository.findById(dto.getAssetId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("존재하지 않는 자산입니다: " + dto.getAssetId())
                    );

            // 이미 폐지된 자산 방어
            if ("N".equals(asset.getAssetStatus())) {
                throw new IllegalStateException("이미 폐지된 자산입니다: " + dto.getAssetId());
            }

            asset.setAssetStatus("N");
            asset.setAssetDesc(dto.getAssetDesc().trim());
        }
    }

}
