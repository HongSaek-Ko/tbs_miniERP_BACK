package com.baek.tbs_miniERP_BACK.controller;

import com.baek.tbs_miniERP_BACK.dto.ApiResponse;
import com.baek.tbs_miniERP_BACK.dto.UpdateMeDTO;
import com.baek.tbs_miniERP_BACK.dto.UserRes;
import com.baek.tbs_miniERP_BACK.service.AuthService;
import com.baek.tbs_miniERP_BACK.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/my")
    public ApiResponse<UserRes> myInfo(Authentication auth) {
        String userId = auth.getName();
        log.info("GET:/my - userId? {}", userId);
        if(userId.isEmpty()) return null;
        return ApiResponse.success(userService.myInfo(userId));
    }

    @PutMapping("/my")
    public ApiResponse<UserRes> updateInfo(Authentication auth, @Valid @RequestBody UpdateMeDTO dto) {
        String userId = auth.getName();
        log.info("PUT:/my - dto? {}", dto.toString());
        if(userId.isEmpty()) return null;
        return ApiResponse.success(userService.updateInfo(userId, dto));
    }

}
