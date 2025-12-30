package com.baek.tbs_miniERP_BACK.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TeamMapper {
    List<String> findAllTeamName();
}
