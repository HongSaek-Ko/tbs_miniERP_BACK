package com.baek.tbs_miniERP_BACK.controller;

import com.baek.tbs_miniERP_BACK.dto.ApiResponse;
import com.baek.tbs_miniERP_BACK.dto.AssetDisposeDTO;
import com.baek.tbs_miniERP_BACK.dto.AssetFilterParams;
import com.baek.tbs_miniERP_BACK.dto.AssetListDTO;
import com.baek.tbs_miniERP_BACK.service.AssetService;
import com.baek.tbs_miniERP_BACK.util.ExcelExporter;
import lombok.RequiredArgsConstructor;
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
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Slf4j
public class AssetController {
    private final AssetService assetService;

    @GetMapping
    public ApiResponse<Page<AssetListDTO>> getAssetList(
            @ModelAttribute AssetFilterParams params,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.success(assetService.getAssetList(params, pageable));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportAssets(@ModelAttribute AssetFilterParams params)
            throws UnsupportedEncodingException {

        List<AssetListDTO> list = assetService.getAssetListForExport(params);

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

    @PatchMapping("/dispose")
    public ApiResponse<?> disposeAssets(@RequestBody List<AssetDisposeDTO> reqs) {

        if (reqs == null || reqs.isEmpty()) {
            return ApiResponse.fail("400", "폐지할 자산이 없습니다.");
        }

        for (AssetDisposeDTO r : reqs) {
            if (r.getAssetId() == null || r.getAssetId().isBlank()) {
                return ApiResponse.fail("400", "자산 ID가 누락되었습니다.");
            }
            if (r.getAssetDesc() == null || r.getAssetDesc().trim().isEmpty()) {
                return ApiResponse.fail("400", "비고란을 입력하세요.");
            }
            if (!r.getAssetDesc().contains("폐지")) {
                return ApiResponse.fail("400", "비고란에 '폐지' 문구가 포함되어야 합니다.");
            }
        }

        assetService.disposeAssets(reqs);
        return ApiResponse.success("disposed");
    }

}
