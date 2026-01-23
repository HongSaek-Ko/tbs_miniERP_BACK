package com.baek.tbs_miniERP_BACK.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="USERS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name="USER_ID", length = 50)
    private String userId;

    @Column(name="USER_NAME", nullable = false, length = 50)
    private String username;

    @Column(name="USER_PW", nullable = false, length = 255)
    private String userPw;

    @Column(name = "USER_STATUS", nullable = false, length = 20)
    private String userStatus; // NEW / ACTIVE / DISABLED

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserAuth> userAuths = new HashSet<>();

    // 권한 부여
    public void grant(Auth auth) {
        UserAuth ua = new UserAuth();
        ua.setId(new UserAuthId(this.userId, auth.getAuthCode()));
        ua.setUser(this);
        ua.setAuth(auth);
        this.userAuths.add(ua);
    }

    // 권한 회수
    public void revoke(String authCode) {
        this.userAuths.removeIf(ua -> ua.getAuth().getAuthCode().equals(authCode));
    }
}
