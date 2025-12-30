package com.baek.tbs_miniERP_BACK.controller;

import com.baek.tbs_miniERP_BACK.service.EmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {
    private final EmpService empService;

    @GetMapping
    public List<String> findAllTeamName() {
        return empService.findAllTeamName();
    }
}
