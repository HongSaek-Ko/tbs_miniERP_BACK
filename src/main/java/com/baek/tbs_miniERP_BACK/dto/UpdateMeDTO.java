package com.baek.tbs_miniERP_BACK.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateMeDTO(@NotBlank @Size(min=1, max=50) String username,
                          @Size(min=8, max=100) String newPassword) {
}
