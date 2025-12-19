package com.baek.tbs_miniERP_BACK.service;

import com.baek.tbs_miniERP_BACK.dto.*;
import com.baek.tbs_miniERP_BACK.entity.Asset;
import com.baek.tbs_miniERP_BACK.entity.Employee;
import com.baek.tbs_miniERP_BACK.entity.Team;
import com.baek.tbs_miniERP_BACK.repository.AssetRepository;
import com.baek.tbs_miniERP_BACK.repository.EmpRepository;
import com.baek.tbs_miniERP_BACK.util.AssetSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;
    private final EmpRepository empRepository;

    // 자산 목록 조회
    public Page<AssetListDTO> getAssetList(AssetFilterParams params, Pageable pageable) {
        Specification<Asset> spec = AssetSpecifications.byFilters(params);

        return assetRepository.findAll(spec, pageable)
                .map(this::toDto);
    }

    // 자산 목록 조회 (엑셀 내보내기용)
    public List<AssetListDTO> getAssetListForExport(AssetFilterParams params) {
        Specification<Asset> spec = AssetSpecifications.byFilters(params);

        return assetRepository.findAll(spec).stream()
                .map(this::toDto)
                .toList();
    }

    // 자산 목록 (Entity) -> (DTO) 변환
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

    // assetId 최대값 찾기
    public String getMaxAssetId(String assetType) {
        return assetRepository.findMaxAssetIdByAssetType(assetType);
    }

    @Transactional
    public void createAsset(AssetCreateDTO req) {
        log.info("AssetCreateDTO: {}",req.toString());
        // 직원 찾기
        Employee emp = empRepository.findById(req.getEmpId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사번입니다."));

        // 날짜 파싱
        LocalDate issuanceDate =
                LocalDate.parse(req.getAssetIssuanceDate());

        LocalDateTime issuance =
                issuanceDate.atStartOfDay(); // 2025-12-14T00:00:00


        // assetId 생성(동시성 방어: 중복이면 재시도)
//        String prefix = req.getAssetType();
//        String assetId;
//        for (int i = 0; i < 3; i++) {
//            Integer max = assetRepository.findMaxAssetIdByAssetType(prefix);
//            int next = (max == null ? 1 : max + 1);
//            assetId = prefix + String.format("%03d", next);
//
//            if (assetRepository.existsByAssetId(assetId)) {
//                continue;
//            }

            Asset a = new Asset();
            log.info("assetId? {}",req.getAssetId());
            a.setAssetId(req.getAssetId());
            a.setAssetType(req.getAssetType());
            a.setAssetManufacturer(req.getAssetManufacturer());
            a.setAssetManufacturedAt(LocalDate.parse(req.getAssetManufacturedAt()).atStartOfDay());
            a.setAssetModelName(req.getAssetModelName());
            a.setAssetSn(req.getAssetSn());
            a.setAssetLoc(req.getAssetLoc());
            a.setAssetIssuanceDate(LocalDate.parse(req.getAssetIssuanceDate()).atStartOfDay());
            a.setAssetDesc(req.getAssetDesc());

            a.setAssetStatus("Y"); // 현재 자산
            a.setEmployee(emp);

            assetRepository.save(a);
//        }
//        throw new IllegalStateException("품번 생성에 실패했습니다. 잠시 후 다시 시도하세요.");
    }

    @Transactional
    public void updateAssets(List<AssetUpdateDTO> dtos) {

        for (AssetUpdateDTO dto : dtos) {
            Asset asset = assetRepository.findById(dto.getAssetId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("존재하지 않는 자산입니다: " + dto.getAssetId())
                    );

            // Asset에서, Employee의 속성이 Employee 타입으로 선언되어 있으므로 setEmp <- emp를 넣어줘야 함
            Employee emp = empRepository.findByEmpIdForUpdate(dto.getEmpId());

            asset.setAssetManufacturer(dto.getAssetManufacturer());
            asset.setAssetManufacturedAt(dto.getAssetManufacturedAt());
            asset.setAssetModelName(dto.getAssetModelName());
//            asset.getEmployee().setEmpId(dto.getEmpId()); // 이거는 해당 Employee의 EmpId(= PK)를 바꾸는 것이므로 불가능.
            asset.setEmployee(emp); // 상술한 방법.
            asset.setAssetSn(dto.getAssetSn());
            asset.setAssetLoc(dto.getAssetLoc());
            asset.setAssetIssuanceDate(dto.getAssetIssuanceDate());
            asset.setAssetDesc(dto.getAssetDesc());
        }
    }
}
