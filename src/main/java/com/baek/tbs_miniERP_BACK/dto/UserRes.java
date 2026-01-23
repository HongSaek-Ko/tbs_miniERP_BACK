package com.baek.tbs_miniERP_BACK.dto;

import java.util.List;

public record UserRes(
        String userId,
        String name,
        String status,
        List<String> auth
) {
}
