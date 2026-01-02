package com.baek.tbs_miniERP_BACK.service;

import com.baek.tbs_miniERP_BACK.dto.*;
import com.baek.tbs_miniERP_BACK.entity.Asset;
import com.baek.tbs_miniERP_BACK.entity.Employee;
import com.baek.tbs_miniERP_BACK.entity.Team;
import com.baek.tbs_miniERP_BACK.mapper.AssetHistoryMapper;
import com.baek.tbs_miniERP_BACK.mapper.AssetMapper;
import com.baek.tbs_miniERP_BACK.repository.AssetRepository;
import com.baek.tbs_miniERP_BACK.repository.EmpRepository;
import com.baek.tbs_miniERP_BACK.util.AssetSpecifications;
import com.baek.tbs_miniERP_BACK.util.SearchTokens;
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
    private final AssetMapper assetMapper;
    private final AssetHistoryMapper assetHistoryMapper;

    // 자산 목록 조회 (JPA)
    public Page<AssetListDTO> getAssetList(AssetFilterParams params, Pageable pageable) {
        Specification<Asset> spec = AssetSpecifications.byFilters(params);

        return assetRepository.findAll(spec, pageable)
                .map(this::toDto);
    }

    // 자산 목록 조회 (Mybatis)
    public List<AssetListDTO> findAll(String assetStatus) {
        return assetMapper.findAll(assetStatus);
    }

    // 자산 목록 조회; 엑셀 내보내기 (JPA)
    public List<AssetListDTO> getAssetListForExport(AssetFilterParams params) {
        Specification<Asset> spec = AssetSpecifications.byFilters(params);

        return assetRepository.findAll(spec).stream()
                .map(this::toDto)
                .toList();
    }

    // 자산 목록 조회; 엑셀 내보내기 (Mybatis)
    public List<AssetListDTO> findAllForExport(AssetFilterParams p) {
        SearchTokens t = SearchTokens.parse(p.getGlobalSearch());
        return assetMapper.findAllForExport(p, t);
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

    // 자산 폐기
    @Transactional
    public void disposeAssets(List<AssetDisposeDTO> reqs) {
//        for (AssetDisposeDTO dto : reqs) {
//            AssetListDTO asset = assetRepository.findById(dto.getAssetId())
//                    .orElseThrow(() ->
//                            new IllegalArgumentException("존재하지 않는 자산입니다: " + dto.getAssetId())
//                    );
//
//            // 이미 폐지된 자산 방어
//            if ("N".equals(asset.getAssetStatus())) {
//                throw new IllegalStateException("이미 폐기된 자산입니다: " + dto.getAssetId());
//            }
//
//            asset.setAssetStatus("N");
//            asset.setAssetDesc(dto.getAssetDesc().trim());
//        }
//        assetMapper.disposeAssets(reqs);
        List<String> ids = reqs.stream().map(AssetDisposeDTO::getAssetId).toList();

        Map<String, AssetSnapshotDTO> beforeMap = assetMapper.findSnapshotByAssetId(ids)
                .stream().collect(Collectors.toMap(AssetSnapshotDTO::getAssetId, x -> x));

        assetMapper.disposeAssets(reqs);

        List<AssetHisCreDTO> histories = reqs.stream().map(d -> {
            AssetSnapshotDTO before = beforeMap.get(d.getAssetId());
            String hold = (before !=null ? before.getHoldEmp() :"(?)");

            return AssetHisCreDTO.builder()
                    .assetId(d.getAssetId())
                    .holdEmpHis(hold)
                    .holdEmp(hold)
                    .historyDesc(d.getAssetDesc())// '폐기' 포함 강제했으니 UI는 isDispose로 칠함
//                    .historyDate(LocalDateTime.now())
                    .build();
        }).toList();
        assetHistoryMapper.createHistory(histories);

    }

    // 이력 조회
    @Transactional
    public List<AssetHistoryListDTO> getAssetHistory(String assetId) {
        return assetHistoryMapper.findByAssetId(assetId);
    }

    // assetId 최대값 찾기 (신규 자산 등록용)
    public String getMaxAssetId(String assetType) {
//        return assetRepository.findMaxAssetIdByAssetType(assetType);
        return assetMapper.findMaxIdByAssetType(assetType);
    }

    // 신규 자산 등록
    @Transactional
    public int createAsset(List<AssetCreateDTO> req) {
        if (req == null || req.isEmpty()) return 0;

        int inserted = assetMapper.createAssets(req);

        List<String> ids = req.stream()
                .map(AssetCreateDTO::getAssetId)
                .filter(s -> s != null && !s.isBlank())
                .distinct()
                .toList();

        if (ids.isEmpty()) return inserted;

        Map<String, AssetSnapshotDTO> afterMap =
                assetMapper.findSnapshotByAssetId(ids).stream()
                        .collect(Collectors.toMap(
                                AssetSnapshotDTO::getAssetId,
                                x -> x,
                                (a, b) -> a
                        ));

        List<AssetHisCreDTO> hcdto = req.stream()
                .map(r -> {
                    AssetSnapshotDTO snap = afterMap.get(r.getAssetId());
                    return AssetHisCreDTO.builder()
                            .assetId(r.getAssetId())
                            .holdEmpHis(null)
                            .holdEmp(snap != null ? snap.getHoldEmp() : null) // null이면 DB/프론트에서 표시 처리 권장
                            .historyDesc(r.getAssetDesc())
                            .build();
                })
                .toList();

        try {
            assetHistoryMapper.createHistory(hcdto);
        } catch (Exception e) {
            Throwable t = e;
            int i = 0;
            while (t != null && i < 10) {
                log.error("cause[{}] {} : {}", i, t.getClass().getName(), t.getMessage());
                t = t.getCause();
                i++;
            }
            throw e;
        }

        return inserted;
//        for(AssetCreateDTO dto : req) {
//            // 직원 찾기
//            Employee emp = empRepository.findById(dto.getEmpId())
//                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사번입니다."));
//
//            // 날짜 파싱
//            LocalDate issuanceDate =
//                    LocalDate.parse(dto.getAssetIssuanceDate());
//
//            LocalDateTime issuance =
//                    issuanceDate.atStartOfDay(); // 2025-12-14T00:00:00
//
//            // getAsset 메서드 돌려서 그대로 save(등록)
//            Asset a = getAsset(dto, emp);
//
//            assetRepository.save(a);
//
//        }
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

//        }
//        throw new IllegalStateException("품번 생성에 실패했습니다. 잠시 후 다시 시도하세요.");
    }

    // 등록용 메서드
    private static Asset getAsset(AssetCreateDTO dto, Employee emp) {
        Asset a = new Asset();
        a.setAssetId(dto.getAssetId());
        a.setAssetType(dto.getAssetType());
        a.setAssetManufacturer(dto.getAssetManufacturer());
        a.setAssetManufacturedAt(LocalDate.parse(dto.getAssetManufacturedAt()).atStartOfDay());
        a.setAssetModelName(dto.getAssetModelName());
        a.setAssetSn(dto.getAssetSn());
        a.setAssetLoc(dto.getAssetLoc());
        a.setAssetIssuanceDate(LocalDate.parse(dto.getAssetIssuanceDate()).atStartOfDay());
        a.setAssetDesc(dto.getAssetDesc());

        a.setAssetStatus("EMPLOYEE"); // 현재 자산
        a.setEmployee(emp);
        return a;
    }

    // 자산 정보 수정
    @Transactional
    public void updateAssets(List<AssetUpdateDTO> dto) {
//        for (AssetUpdateDTO dto : dtos) {
//            Asset asset = assetRepository.findById(dto.getAssetId())
//                    .orElseThrow(() ->
//                            new IllegalArgumentException("존재하지 않는 자산입니다: " + dto.getAssetId())
//                    );
//
//            // Asset에서, Employee의 속성이 Employee 타입으로 선언되어 있으므로 setEmp <- emp를 넣어줘야 함
//            Employee emp = empRepository.findByEmpIdForUpdate(dto.getEmpId());
//
//            asset.setAssetManufacturer(dto.getAssetManufacturer());
//            asset.setAssetManufacturedAt(dto.getAssetManufacturedAt());
//            asset.setAssetModelName(dto.getAssetModelName());
//            // asset.getEmployee().setEmpId(dto.getEmpId()); // 이거는 해당 Employee의 EmpId(= PK)를 바꾸는 것이므로 불가능.
//            asset.setEmployee(emp); // 상술한 방법.
//            asset.setAssetSn(dto.getAssetSn());
//            asset.setAssetLoc(dto.getAssetLoc());
//            asset.setAssetIssuanceDate(dto.getAssetIssuanceDate());
//            asset.setAssetDesc(dto.getAssetDesc());
//        }
        List<String> ids = dto.stream().map(AssetUpdateDTO::getAssetId).toList();

        // 수정 전
        Map<String, AssetSnapshotDTO> beforeMap = assetMapper.findSnapshotByAssetId(ids).stream().collect(Collectors.toMap(AssetSnapshotDTO::getAssetId, x -> x));

        // 수정 처리
        assetMapper.updateAssets(dto);

        // 수정 후
        Map<String, AssetSnapshotDTO> afterMap = assetMapper.findSnapshotByAssetId(ids).stream().collect(Collectors.toMap(AssetSnapshotDTO::getAssetId, x -> x));

        // 이력 생성
        List<AssetHisCreDTO> hdto = dto.stream().map(d -> {
            AssetSnapshotDTO before = beforeMap.get(d.getAssetId());
            AssetSnapshotDTO after = afterMap.get(d.getAssetId());
            String beforeHold = (before != null ? before.getHoldEmp() : null);
            String afterHold = (after != null ? after.getHoldEmp() : d.getEmpId() + " (?)");
            return AssetHisCreDTO.builder().assetId(d.getAssetId()).holdEmpHis(beforeHold).holdEmp(afterHold).historyDesc(d.getAssetDesc()).build();
        }).toList();
        assetHistoryMapper.createHistory(hdto);
    }

    // 자산 (변동) 이력 조회
    public List<AssetHistoryListDTO> findByAssetId(String assetId) {
        return assetHistoryMapper.findByAssetId(assetId);
    }
}
