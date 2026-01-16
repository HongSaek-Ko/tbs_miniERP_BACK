package com.baek.tbs_miniERP_BACK.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "AUTH")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auth {

    @Id
    @Column(name = "AUTH_CODE", length = 30)
    private String authCode; // AUTH_FA, AUTH_HR, AUTH_ADMIN

    @Column(name = "AUTH_NAME", nullable = false, length = 50)
    private String authName;
}