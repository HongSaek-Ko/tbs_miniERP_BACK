package com.baek.tbs_miniERP_BACK.controller;

import com.baek.tbs_miniERP_BACK.service.EmpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emp")
@RequiredArgsConstructor
@Slf4j
public class EmpController {
    private final EmpService empService;
}
