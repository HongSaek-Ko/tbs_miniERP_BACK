package com.baek.tbs_miniERP_BACK.service;

import com.baek.tbs_miniERP_BACK.config.JwtProvider;
import com.baek.tbs_miniERP_BACK.dto.SignupDTO;
import com.baek.tbs_miniERP_BACK.dto.UpdateMeDTO;
import com.baek.tbs_miniERP_BACK.dto.UserRes;
import com.baek.tbs_miniERP_BACK.entity.User;
import com.baek.tbs_miniERP_BACK.mapper.AuthMapper;
import com.baek.tbs_miniERP_BACK.mapper.UserAuthMapper;
import com.baek.tbs_miniERP_BACK.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserAuthMapper userAuthMapper;
    private final PasswordEncoder passwordEncoder;

    // 내 정보
    public UserRes myInfo(String userId) {
        // 유저 정보
        User user = userMapper.findUserByUserId(userId);
        if(user == null || user.getUserId().isEmpty()) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        // 유저 권한 코드
        List<String> authCodes = userMapper.findAuthByUserId(userId);

        // 권한 코드 -> 권한명
        List<String> perms = new ArrayList<>(AuthMapper.toPerms(new HashSet<>(authCodes)));

        return new UserRes(user.getUserId(), user.getUsername(), user.getUserStatus(), perms);
    }

    // 유저 정보 수정
    public UserRes updateInfo(String userId, UpdateMeDTO dto) {
        int result = userMapper.updateUsername(userId, dto.username());
        if(result == 0) throw new RuntimeException("유저 이름 업데이트 중 오류 발생");
        if(dto.newPassword() != null && !dto.newPassword().isEmpty()) {
            int pwRes = userMapper.updatePassword(userId, passwordEncoder.encode(dto.newPassword()));
            if(pwRes == 0) throw new RuntimeException("유저 비밀번호 업데이트 중 오류 발생");
        }

        // 업데이트 이후 정보 재조회
        User user = userMapper.findUserByUserId(userId);
        user.setUserStatus("ACTIVE"); // 업데이트 시 NEW(이든 뭐든) -> ACTIVE로 변경
        List<String> authCodes = userMapper.findAuthByUserId(userId);
        List<String> perms = new ArrayList<>(AuthMapper.toPerms(new HashSet<>(authCodes)));

        return new UserRes(user.getUserId(), user.getUsername(), user.getUserStatus(), perms);
    }

    // 유저 권한 부여
    public int grantAuth(String userId, String authCode) {
        return userAuthMapper.grantAuth(userId, authCode);
    }

    // 유저 권한 회수
    public int withdrawAuth(String userId, String authCode) {
        return userAuthMapper.withdrawAuth(userId, authCode);
    }
}
