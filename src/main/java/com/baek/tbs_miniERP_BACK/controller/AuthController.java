package com.baek.tbs_miniERP_BACK.controller;

import com.baek.tbs_miniERP_BACK.dto.ApiResponse;
import com.baek.tbs_miniERP_BACK.dto.LoginDTO;
import com.baek.tbs_miniERP_BACK.dto.LoginRes;
import com.baek.tbs_miniERP_BACK.dto.SignupDTO;
import com.baek.tbs_miniERP_BACK.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<?> signup(@Valid @RequestBody SignupDTO dto) {
        log.info("POST:/auth - dto: {}",dto.toString());
        int res = authService.singup(dto);
        if(res != 1) return ApiResponse.fail("500", "회원가입 실패.");
        return ApiResponse.success("회원가입 성공.");
    }

    @PostMapping("/login")
    public ApiResponse<LoginRes> login(@Valid @RequestBody LoginDTO dto) {
        log.info("POST:/login - dto: {}",dto.toString());
        return ApiResponse.success(authService.login(dto));
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout() {
        return ApiResponse.success("로그아웃 완료.");
    }
}
