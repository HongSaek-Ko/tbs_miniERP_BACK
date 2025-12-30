package com.baek.tbs_miniERP_BACK.service;

import com.baek.tbs_miniERP_BACK.dto.*;
import com.baek.tbs_miniERP_BACK.entity.Employee;
import com.baek.tbs_miniERP_BACK.mapper.EmpMapper;
import com.baek.tbs_miniERP_BACK.mapper.TeamMapper;
import com.baek.tbs_miniERP_BACK.repository.EmpRepository;
import com.baek.tbs_miniERP_BACK.repository.TeamRepository;
import com.baek.tbs_miniERP_BACK.util.EmpSpecifications;
import com.baek.tbs_miniERP_BACK.util.SearchTokens;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmpService {
    private final EmpRepository empRepository;
    private final TeamRepository teamRepository;
    private final EmpMapper empMapper;
    private final TeamMapper teamMapper;

    public List<EmpDTO> findAll() {
        return empMapper.findAll();
    }

    // JPA 버전
//    public List<EmpDTO> findAllEmp() {
//        return empRepository.findAll().stream().map(emp -> EmpDTO.builder()
//                .empId(emp.getEmpId())
//                .empName(emp.getEmpName())
//                .empPos(emp.getEmpPos())
//                .empStatus(Objects.equals(emp.getEmpStatus(), "EMPLOYEE") ? "재직" : Objects.equals(emp.getEmpStatus(), "RESIGN") ? "퇴직" : Objects.equals(emp.getEmpStatus(), "ONLEAVE") ? "휴직" : "기타")
//                .teamName(emp.getTeam().getTeamName()).build()
//        ).toList();
//    }

    // 엑셀 내보내기용 직원 목록 조회(필터 반영)
//    public List<EmpDTO> getEmpListForExport(EmpFilterParams params) {
//        Specification<Employee> spec = EmpSpecifications.byFilters(params);
//
//        return empRepository.findAll(spec).stream()
//                .map(this::toDto)// 기존 매핑 함수 사용
//                .toList();
//    }
    public List<EmpDTO> selectEmpForExport(EmpFilterParams p) {
        SearchTokens t = SearchTokens.parse(p.getGlobalSearch());
        return empMapper.selectEmpForExport(p, t);
    }

    // Employee Entity -> DTO
    private EmpDTO toDto(Employee e) {
        EmpDTO dto = new EmpDTO();
        dto.setEmpId(e.getEmpId());
        dto.setEmpName(e.getEmpName());
        dto.setEmpPos(e.getEmpPos());
        dto.setTeamName(e.getTeam().getTeamName());// 또는 e.getTeam().getTeamName()
        dto.setEmpStatus(e.getEmpStatus());
        return dto;
    }


    // 직원 정보 업데이트
//    @Transactional
//    public void updateEmps(List<EmpUpdateDTO> dtos) {
//        log.info("asdf");
//        for(EmpUpdateDTO dto : dtos) {
//            Employee employee = empRepository.findById(dto.getEmpId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사원입니다." + dto.getEmpId()));
//            employee.setEmpName(dto.getEmpName());
//            employee.setEmpPos(dto.getEmpPos());
//            employee.setTeam(teamRepository.findByTeamName(dto.getTeamName()));
//            employee.setEmpStatus(dto.getEmpStatus());
//        }
//    }
    @Transactional
    public void updateEmps(List<EmpUpdateDTO> dtos) {
        empMapper.updateEmps(dtos);
    }

    // 직원 퇴사처리
//    @Transactional
//    public void resignEmps(List<EmpResignDTO> dtos) {
//        for(EmpResignDTO dto : dtos) {
//            Employee emp = empRepository.findById(dto.getEmpId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사원입니다." + dto.getEmpId()));
//            if(emp.getEmpStatus().contains("퇴사") || emp.getEmpStatus().toLowerCase().contains("resign") || emp.getEmpStatus().toLowerCase().contains("retire")) {
//                throw new IllegalStateException("이미 퇴사한 사원입니다."+dto.getEmpId());
//            }
//
//            emp.setEmpStatus("RESIGN");
//        }
//    }
    // 직원 퇴사처리
    public void resignEmps(List<EmpResignDTO> dtos) {
        log.info(dtos.toString());
        empMapper.resignEmps(dtos);
    }

    public List<String> findAllTeamName() {
        log.info("팀 명단: {}", teamMapper.findAllTeamName().toString());
        return teamMapper.findAllTeamName();
    }
}
