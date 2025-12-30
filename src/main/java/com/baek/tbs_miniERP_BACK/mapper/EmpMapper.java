package com.baek.tbs_miniERP_BACK.mapper;

import com.baek.tbs_miniERP_BACK.dto.EmpDTO;
import com.baek.tbs_miniERP_BACK.dto.EmpFilterParams;
import com.baek.tbs_miniERP_BACK.dto.EmpResignDTO;
import com.baek.tbs_miniERP_BACK.dto.EmpUpdateDTO;
import com.baek.tbs_miniERP_BACK.util.SearchTokens;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmpMapper {

    // 전체 조회
    List<EmpDTO> findAll();

    // 전체 조회 (엑셀 추출용)
    List<EmpDTO> selectEmpForExport(@Param("p") EmpFilterParams p, @Param("t") SearchTokens t);

    // 직원 정보 수정
    void updateEmps(List<EmpUpdateDTO> dtos);

    // 직원 정보 수정; 퇴사처리
    void resignEmps(List<EmpResignDTO> dtos);
}
