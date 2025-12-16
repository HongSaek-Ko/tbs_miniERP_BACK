package com.baek.tbs_miniERP_BACK.controller;

import com.baek.tbs_miniERP_BACK.dto.ApiResponse;
import com.baek.tbs_miniERP_BACK.dto.EmpDTO;
import com.baek.tbs_miniERP_BACK.service.EmpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/emp")
@RequiredArgsConstructor
@Slf4j
public class EmpController {
    private final EmpService empService;

    @GetMapping
    public ApiResponse<List<EmpDTO>> findAllEmp() {
        return ApiResponse.success(empService.findAllEmp());
    }
}
