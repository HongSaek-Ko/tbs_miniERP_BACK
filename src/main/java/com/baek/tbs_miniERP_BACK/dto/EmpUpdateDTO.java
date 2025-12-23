package com.baek.tbs_miniERP_BACK.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpUpdateDTO {
    private String empId;
    private String empName;
    private String empPos;
    private String teamName; // 프론트에서 teamName으로 던지므로 teamName -> findByTeamName -> team으로 반환해야 함
    private String empStatus;
}
