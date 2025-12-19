package com.baek.tbs_miniERP_BACK.controller;

import com.baek.tbs_miniERP_BACK.dto.AssetFilterParams;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@ToString
@RequestMapping("/api/excel")
public class ExcelController {

    private final AssetController assetController;
    @GetMapping
    public void excelExport(@ModelAttribute Map<String, String> params) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if(params.containsKey("assetType")) {
            AssetFilterParams assetFilterParams = mapper.convertValue(params, AssetFilterParams.class);
            assetController.exportAssets(assetFilterParams);
        }
    }
}
