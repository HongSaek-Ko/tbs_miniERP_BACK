package com.baek.tbs_miniERP_BACK.controller;

import com.baek.tbs_miniERP_BACK.dto.ApiResponse;
import com.baek.tbs_miniERP_BACK.dto.AssetListDTO;
import com.baek.tbs_miniERP_BACK.service.AssetService;
import com.baek.tbs_miniERP_BACK.util.ExcelExporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Slf4j
public class AssetController {
    private final AssetService assetService;

    @GetMapping
    public ApiResponse<Page<AssetListDTO>> getAssetList(@RequestParam(required = false) String assetType,
                                                        @RequestParam(required = false) String empName,
                                                        @RequestParam(required = false) String empPos,
                                                        @RequestParam(required = false) Integer page,
                                                        @RequestParam(required = false) Integer size
    ) {
        return ApiResponse.success(assetService.getAssetList(assetType, empName, empPos, page, size));
    }

    // 엑셀 Export
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportAssets(
            @RequestParam(required = false) String assetType,
            @RequestParam(required = false) String empName,
            @RequestParam(required = false) String empPos
    ) {
        List<AssetListDTO> list =
                assetService.getAssetListForExport(assetType, empName, empPos);

        byte[] bytes = ExcelExporter.export(list);

        String filename = "자산_목록_" + LocalDate.now() + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(bytes);
    }
}
