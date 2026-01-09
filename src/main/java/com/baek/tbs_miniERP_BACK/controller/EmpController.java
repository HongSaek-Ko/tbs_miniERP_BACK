package com.baek.tbs_miniERP_BACK.controller;

import com.baek.tbs_miniERP_BACK.dto.*;
import com.baek.tbs_miniERP_BACK.service.EmpService;
import com.baek.tbs_miniERP_BACK.util.EmpExcelExporter;
import com.baek.tbs_miniERP_BACK.util.ExcelExporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/emp")
@RequiredArgsConstructor
@Slf4j
public class EmpController {
    private final EmpService empService;

    // 직원 전체 목록
    @GetMapping
    public ApiResponse<List<EmpDTO>> findAll() {
        return ApiResponse.success(empService.findAll());
    }

    // 엑셀 추출
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportEmp(@ModelAttribute EmpFilterParams params) throws UnsupportedEncodingException {
//        List<EmpDTO> dtos = empService.getEmpListForExport(params); // jpa 버전
        List<EmpDTO> dtos = empService.selectEmpForExport(params);
        byte[] bytes = EmpExcelExporter.export(dtos);
        String filename = "직원_목록_" + LocalDate.now() + ".xlsx";
        String encoded = URLEncoder.encode(filename, "UTF-8");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encoded + "\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                ))
                .body(bytes);
    }

    // 직원 다음 번호 조회 (등록 폼용)
    @GetMapping("/nextId")
    public ApiResponse<String> nextEmpId() {
        String maxId = empService.nextEmpId(); // 예: E009
        String code = maxId.substring(0,1); // E
        int nextId = Integer.parseInt(maxId.replaceAll("[^0-9]]", ""))+1; // 10
        DecimalFormat df = new DecimalFormat("000"); // 포맷 지정
        return ApiResponse.success(code+df.format(nextId)); // E + 10->010 => E010
    }

    // 전체 사번 조회 (등록 폼용)
    @GetMapping("/eIds")
    public ApiResponse<List<String>> findAllEmpId() {
        List<String> eIds = empService.findAllEmpId();
            return ApiResponse.success(eIds);
    }

    // 직원 정보 등록
    @PostMapping
    public ApiResponse<?> createEmps(@RequestBody List<EmpCreateDTO> reqs) {
         int res = empService.createEmps(reqs);
        if(res > 0) {
            return ApiResponse.success("등록 성공");
        } else {
            return ApiResponse.fail("500", "등록 실패");
        }
    }
    // 직원 정보 수정
    @PatchMapping("/bulkUpdate")
    public ApiResponse<?> empUpdate(@RequestBody List<EmpUpdateDTO> dtos) {
        log.info(dtos.toString());
        empService.updateEmps(dtos);
        return ApiResponse.success("수정 성공");
    }

    // 직원 정보 수정 - 퇴직처리
    @PatchMapping("/resign")
    public ApiResponse<?> empResign(@RequestBody List<EmpResignDTO> dtos) {
        if(dtos == null || dtos.isEmpty()) {
            return ApiResponse.fail("400", "퇴사처리 대상이 없습니다.");
        }

        for(EmpResignDTO dto : dtos) {
            if(dto.getEmpId() == null || dto.getEmpId().isEmpty()) {
                return ApiResponse.fail("400", "사번이 누락되었습니다.");
            }
            if (dto.getEmpStatus() == null || dto.getEmpStatus().isEmpty()) {
                return ApiResponse.fail("400", "직원 상태를 입력하세요.");
            }
            if (!dto.getEmpStatus().contains("퇴사")) {
                return ApiResponse.fail("400", "'퇴사' 문구가 포함되어야 합니다.");
            }
        }
        empService.resignEmps(dtos);
        return ApiResponse.success("퇴사처리 완료");
    }

    // 직위 전체 조회
    @GetMapping("/empPos")
    public ApiResponse<List<String>> findAllEmpPos() {
        log.info("직위 명단: {}", empService.findAllEmpPos().toString());
        return ApiResponse.success(empService.findAllEmpPos());
    }
}
