package com.baek.tbs_miniERP_BACK.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpFilterParams {
    // 엑셀 내보내기 - 직원 목록
    private String empId;
    private String empName;
    private String empPos;
    private String teamName;
    private String empStatus;
    private String globalSearch;
}
