package com.baek.tbs_miniERP_BACK.service;

import com.baek.tbs_miniERP_BACK.dto.EmpDTO;
import com.baek.tbs_miniERP_BACK.entity.Employee;
import com.baek.tbs_miniERP_BACK.repository.EmpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmpService {
    private final EmpRepository empRepository;

    public List<EmpDTO> findAllEmp() {
        return empRepository.findAll().stream().map(emp -> EmpDTO.builder()
                .empId(emp.getEmpId())
                .empName(emp.getEmpName())
                .empPos(emp.getEmpPos())
                .empStatus(Objects.equals(emp.getEmpStatus(), "EMPLOYEE") ? "재직" : Objects.equals(emp.getEmpStatus(), "RETIREMENT") ? "퇴직" : Objects.equals(emp.getEmpStatus(), "ONLEAVE") ? "휴직" : "기타")
                .teamName(emp.getTeam().getTeamName()).build()
        ).toList();
    }

}
