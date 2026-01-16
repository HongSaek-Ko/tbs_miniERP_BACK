package com.baek.tbs_miniERP_BACK.dto;

public record LoginRes(
        String accessToken,
        UserRes user
) {
}
