package com.baek.tbs_miniERP_BACK.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserAuthId implements Serializable {

    @Column(name = "USER_ID", length = 50)
    private String userId;

    @Column(name = "AUTH_CODE", length = 30)
    private String authCode;
}
