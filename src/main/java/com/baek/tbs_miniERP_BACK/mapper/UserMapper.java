package com.baek.tbs_miniERP_BACK.mapper;

import com.baek.tbs_miniERP_BACK.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    // 유저 존재 여부 확인
    int isExistUser(@Param("userId") String userId);
    // 유저 ID로 유저 찾기
    User findUserByUserId(@Param("userId") String userId);
    // 권한 코드 목록 조회
    List<String> findAuthByUserId(@Param("userId") String userId);
    // 회원가입
    int createUser(User user);
    // 이름 수정
    int updateUsername(@Param("userId") String userId, @Param("username")String username);
    // 비밀번호 수정
    int updatePassword(@Param("userId") String userId, @Param("userPw")String userPw);
}
