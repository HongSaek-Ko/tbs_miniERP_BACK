package com.baek.tbs_miniERP_BACK.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class UserWithAuthDTO {
    private String userId;
    private String userName;
    private String auths; // '인사관리, 자산관리, ...' (, 로 연결된 String. Array 아님)
}
