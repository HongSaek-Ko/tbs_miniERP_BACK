package com.baek.tbs_miniERP_BACK.dto;

import com.baek.tbs_miniERP_BACK.entity.Employee;
import com.baek.tbs_miniERP_BACK.repository.EmpRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Slf4j
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AssetUpdateDTO {
//    private final EmpRepository empRepository;

    private String assetId;
    private String assetManufacturer;
    private LocalDateTime assetManufacturedAt;
    private String assetModelName;
    private String empId;
    private String assetSn;
    private String assetLoc;
    private LocalDateTime assetIssuanceDate;
    private String assetDesc;

//    public Employee toEntity() {
//        log.info("toEntity() 실행!!");
//        Employee newEmp = empRepository.findByEmpIdForUpdate(empId);
//        log.info("newEmp: {}",newEmp.toString());
//        return Employee.builder().empId(empId).empName(newEmp.get().getEmpName()).empPos(newEmp.get().getEmpPos()).empRegDt(newEmp.get().getEmpRegDt()).empStatus(newEmp.get().getEmpStatus()).build();
//        return newEmp;
//    }
}
