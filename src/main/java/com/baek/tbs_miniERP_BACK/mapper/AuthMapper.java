package com.baek.tbs_miniERP_BACK.mapper;

import java.util.HashSet;
import java.util.Set;

public final class AuthMapper {
    private AuthMapper() {}

    public static Set<String> toPerms(Set<String> authCodes) {
        Set<String> perms = new HashSet<>();
        if (authCodes.contains("AUTH_FA")) perms.add(Perms.ASSET_WRITE);
        if (authCodes.contains("AUTH_HR")) perms.add(Perms.HR_WRITE);
        if (authCodes.contains("AUTH_ADMIN")) {
            perms.add(Perms.ASSET_WRITE);
            perms.add(Perms.HR_WRITE);
            perms.add(Perms.ADMIN);
        }
        return perms;
    }
}
