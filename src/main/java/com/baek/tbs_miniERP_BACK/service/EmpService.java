package com.baek.tbs_miniERP_BACK.service;

import com.baek.tbs_miniERP_BACK.dto.EmpDTO;
import com.baek.tbs_miniERP_BACK.dto.EmpResignDTO;
import com.baek.tbs_miniERP_BACK.dto.EmpUpdateDTO;
import com.baek.tbs_miniERP_BACK.entity.Employee;
import com.baek.tbs_miniERP_BACK.repository.EmpRepository;
import com.baek.tbs_miniERP_BACK.repository.TeamRepository;
import jakarta.transaction.Transactional;
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
    private final TeamRepository teamRepository;

    public List<EmpDTO> findAllEmp() {
        return empRepository.findAll().stream().map(emp -> EmpDTO.builder()
                .empId(emp.getEmpId())
                .empName(emp.getEmpName())
                .empPos(emp.getEmpPos())
                .empStatus(Objects.equals(emp.getEmpStatus(), "EMPLOYEE") ? "재직" : Objects.equals(emp.getEmpStatus(), "RETIREMENT") ? "퇴직" : Objects.equals(emp.getEmpStatus(), "ONLEAVE") ? "휴직" : "기타")
                .teamName(emp.getTeam().getTeamName()).build()
        ).toList();
    }

    // 직원 정보 업데이트
    @Transactional
    public void updateEmps(List<EmpUpdateDTO> dtos) {
        for(EmpUpdateDTO dto : dtos) {
            Employee employee = empRepository.findById(dto.getEmpId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사원입니다." + dto.getEmpId()));
            employee.setEmpName(dto.getEmpName());
            employee.setEmpPos(dto.getEmpPos());
            employee.setTeam(teamRepository.findById(dto.getTeamId()).orElseThrow(() -> new RuntimeException("존재하지 않는 소속입니다.")));
            employee.setEmpStatus(dto.getEmpStatus());
        }
    }

    // 직원 퇴사처리
    @Transactional
    public void resignEmps(List<EmpResignDTO> dtos) {
        for(EmpResignDTO dto : dtos) {
            Employee emp = empRepository.findById(dto.getEmpId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사원입니다." + dto.getEmpId()));
            if(emp.getEmpStatus().contains("퇴사") || emp.getEmpStatus().toLowerCase().contains("resign") || emp.getEmpStatus().toLowerCase().contains("retire")) {
                throw new IllegalStateException("이미 퇴사한 사원입니다."+dto.getEmpId());
            }

            emp.setEmpStatus("RESIGN");
        }
    }
}
