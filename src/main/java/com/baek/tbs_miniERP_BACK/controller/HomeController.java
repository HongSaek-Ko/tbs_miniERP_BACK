package com.baek.tbs_miniERP_BACK.controller;

import com.baek.tbs_miniERP_BACK.dto.TestDTO;
import com.baek.tbs_miniERP_BACK.entity.Test;
import com.baek.tbs_miniERP_BACK.service.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/test")
    public List<Test> test() {
        return homeService.findAll();
    }
}
