package com.baek.tbs_miniERP_BACK.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmpCreateDTO {
    private String empId;
    private String empName;
    private String empPos;
    private String empRegDt; // 입사일자
    private String teamName; // INSERT ...teamName = (SELECT teamId FROM team WHERE teamName = #{teamName}) ...
}
