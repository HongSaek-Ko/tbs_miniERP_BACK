package com.baek.tbs_miniERP_BACK.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAuthMapper {
    // 권한 부여
    int grantAuth(@Param("userId")String userId, @Param("authCode")String authCode);
    // 권한 회수
    int withdrawAuth(@Param("userId")String userId, @Param("authCode")String authCode);
}
