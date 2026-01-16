package com.baek.tbs_miniERP_BACK.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @NotBlank @Size(min=4, max=50) String userId,
        @NotBlank @Size(min=8, max=100) String userPw
) {}
