package com.baek.tbs_miniERP_BACK.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpUpdateDTO {
    private String empId;
    private String empName;
    private String empPos;
    private String teamId;
    private String empStatus;
}
