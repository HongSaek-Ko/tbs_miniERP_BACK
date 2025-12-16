package com.baek.tbs_miniERP_BACK.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmpDTO {
    private String empId; // 사번 (언젠간 쓸거임)
    private String empName; // 성명
    private String empPos; // 직위
    private String empStatus; // 재직상태 (언젠간 쓸거임)
    private String teamName; // 소속
}
