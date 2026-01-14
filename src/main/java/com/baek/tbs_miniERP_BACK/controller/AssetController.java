package com.baek.tbs_miniERP_BACK.controller;

import com.baek.tbs_miniERP_BACK.dto.*;
import com.baek.tbs_miniERP_BACK.mapper.AssetMapper;
import com.baek.tbs_miniERP_BACK.service.AssetService;
import com.baek.tbs_miniERP_BACK.util.ExcelExporter;
import com.baek.tbs_miniERP_BACK.util.HisExcelExporter;
import com.baek.tbs_miniERP_BACK.util.TotalHisExporter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Slf4j
@ToString
public class AssetController {
    private final AssetService assetService;

    // 자산 목록 조회 (페이징 포함) (JPA)
//    @GetMapping
//    public ApiResponse<Page<AssetListDTO>> getAssetList(
//            @ModelAttribute AssetFilterParams params,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "50") int size
//    ) {
//        log.info("params? {}", params.toString());
//        Pageable pageable = PageRequest.of(page, size);
//        return ApiResponse.success(assetService.getAssetList(params, pageable));
//    }

    // 자산 목록 조회 (페이징 포함) (Mybatis)
    @GetMapping
    public ApiResponse<List<AssetListDTO>> getAssetList(@RequestParam("assetStatus") String assetStatus
    ) {
        return ApiResponse.success(assetService.findAll(assetStatus));
    }

    // 엑셀 내보내기(자산)
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportAssets(@ModelAttribute AssetFilterParams params)
            throws UnsupportedEncodingException {
        // List<AssetListDTO> list = assetService.getAssetListForExport(params); // JPA
        List<AssetListDTO> list = assetService.findAllForExport(params); // Mybatis

        byte[] bytes = ExcelExporter.export(list);

        String filename = "자산_목록_" + LocalDate.now() + ".xlsx";
        String encoded = URLEncoder.encode(filename, "UTF-8");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encoded + "\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                ))
                .body(bytes);
    }

    // 자산 폐기
    @PatchMapping("/dispose")
    public ApiResponse<?> disposeAssets(@RequestBody List<AssetDisposeDTO> reqs) {

        if (reqs == null || reqs.isEmpty()) {
            return ApiResponse.fail("400", "폐기할 자산이 없습니다.");
        }

        for (AssetDisposeDTO r : reqs) {
            if (r.getAssetId() == null || r.getAssetId().isBlank()) {
                return ApiResponse.fail("400", "자산 ID가 누락되었습니다.");
            }
            if (r.getAssetDesc() == null || r.getAssetDesc().trim().isEmpty()) {
                return ApiResponse.fail("400", "비고란을 입력하세요.");
            }
            if (!r.getAssetDesc().contains("폐기")) {
                return ApiResponse.fail("400", "비고란에 '폐기' 문구가 포함되어야 합니다.");
            }
        }

        assetService.disposeAssets(reqs);
        return ApiResponse.success("자산 폐기 완료");
    }

    // 자산 시리얼 번호 조회 (유효성 검증)
    @GetMapping("/sn")
    public ApiResponse<List<String>> findAllSn() {
        return ApiResponse.success(assetService.findAllSn());
    }

    // 자산 다음 번호 조회 (등록 폼용)
    @GetMapping("/nextId")
    public ApiResponse<String> nextAssetId(String assetType) {
        String maxId = assetService.getMaxAssetId(assetType);
        String typeCode = maxId.substring(0,1);
        int nextId = Integer.parseInt(maxId.replaceAll("[^0-9]", ""))+1;
        DecimalFormat df = new DecimalFormat("000");
        return ApiResponse.success(typeCode+df.format(nextId));
    }

    // 신규 자산 등록 (다중)
    @PostMapping
    public ApiResponse<?> create(@Valid @RequestBody List<AssetCreateDTO> req) {
        int res = assetService.createAsset(req);
        if(res > 0) {
            return ApiResponse.success("등록 성공");
        } else {
            return ApiResponse.fail("500", "등록 실패");
        }
    }

    // 자산 정보 수정 (다중)
    @PatchMapping("/bulkUpdate")
    public ApiResponse<?> assetUpdate(@RequestBody List<AssetUpdateDTO> dto) {
        log.info(dto.toString());
        assetService.updateAssets(dto);
        return ApiResponse.success("수정 성공");
    }

    // 자산별 변동 이력 조회
    @GetMapping("/history/{assetId}")
    public ApiResponse<List<AssetHistoryListDTO>> findByAssetId(@PathVariable("assetId") String assetId) {
        log.info("assetId? {}", assetId);
        List<AssetHistoryListDTO> dto;
        if(!assetId.equals("TOTAL")) {
            dto = assetService.findByAssetId(assetId);
        } else {
            dto = assetService.findAllHistories();
        }
        return ApiResponse.success(dto);
    }

    // 엑셀 내보내기(개별 자산 이력)
    @GetMapping("/history/export/{assetId}")
    public ResponseEntity<byte[]> exportAssetHistoryByAssetId(@PathVariable("assetId") String assetId)
            throws UnsupportedEncodingException {
        List<AssetHistoryListDTO> list = assetService.findByAssetId(assetId); // Mybatis

        byte[] bytes = HisExcelExporter.export(list);

        String filename = assetId +"_변동 이력_" + LocalDate.now() + ".xlsx";
        String encoded = URLEncoder.encode(filename, "UTF-8");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encoded + "\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                ))
                .body(bytes);
    }

    // 엑셀 내보내기(전체 자산 이력)
    @GetMapping("/history/export")
    public ResponseEntity<byte[]> exportAssetHistory(@ModelAttribute AssetHisFilterParams params)
            throws UnsupportedEncodingException {
        log.info("params? {}", params.toString());
        List<AssetHistoryListDTO> list = assetService.findAllForExport(params); // Mybatis

//        byte[] bytes = HisExcelExporter.export(list);
        byte[] bytes = TotalHisExporter.export(list);

        String filename = "변동 이력_" + LocalDate.now() + ".xlsx";
        String encoded = URLEncoder.encode(filename, "UTF-8");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encoded + "\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                ))
                .body(bytes);
    }
}
