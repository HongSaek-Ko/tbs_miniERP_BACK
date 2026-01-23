package com.baek.tbs_miniERP_BACK.service;

import com.baek.tbs_miniERP_BACK.dto.EmpCreateDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmpOnboardingService {
    private final EmpService empService;
    private final AuthService authService;

    @Transactional
    public int createEmpWithUser(List<EmpCreateDTO> dtos) {
        int res1 = empService.createEmps(dtos);
        int res2 = authService.singup(dtos);
        if (res1 > 0 && res2 > 0) return 1;
        return 0;
    }
}
