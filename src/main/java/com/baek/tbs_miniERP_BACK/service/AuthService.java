package com.baek.tbs_miniERP_BACK.service;

import com.baek.tbs_miniERP_BACK.config.JwtProvider;
import com.baek.tbs_miniERP_BACK.dto.LoginDTO;
import com.baek.tbs_miniERP_BACK.dto.LoginRes;
import com.baek.tbs_miniERP_BACK.dto.SignupDTO;
import com.baek.tbs_miniERP_BACK.dto.UserRes;
import com.baek.tbs_miniERP_BACK.entity.User;
import com.baek.tbs_miniERP_BACK.mapper.AuthMapper;
import com.baek.tbs_miniERP_BACK.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    // id(중복체크 O), name, pw(암호화) 입력받아 저장
    public int singup(SignupDTO dto) {
        if(userMapper.isExistUser(dto.userId()) > 0) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        User user = new User();
        user.setUserId(dto.userId());
        user.setUsername(dto.username());
        user.setUserPw(passwordEncoder.encode(dto.userPw()));
        return userMapper.createUser(user);
    }

    // 로그인
    // 리턴값 (AT, 유저(id, name, auth))
    public LoginRes login(LoginDTO dto) {
        User user = userMapper.findUserByUserId(dto.userId());
        if(user == null || !passwordEncoder.matches(dto.userPw(), user.getUserPw())) {
            throw new IllegalArgumentException("아이디/비밀번호가 일치하지 않습니다.");
        }

        List<String> authCodes = userMapper.findAuthByUserId(dto.userId());
        List<String> perms = new ArrayList<>(AuthMapper.toPerms(new HashSet<>(authCodes)));
        String token = jwtProvider.createAccessToken(user.getUserId(), user.getUsername(), perms);

        return new LoginRes(token, new UserRes(user.getUserId(), user.getUsername(), perms));
    }
}
