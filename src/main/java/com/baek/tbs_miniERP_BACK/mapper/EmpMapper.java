package com.baek.tbs_miniERP_BACK.mapper;

import com.baek.tbs_miniERP_BACK.dto.EmpDTO;
import com.baek.tbs_miniERP_BACK.dto.EmpResignDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpMapper {

    List<EmpDTO> findAll();

    void resignEmps(List<EmpResignDTO> dtos);
}
