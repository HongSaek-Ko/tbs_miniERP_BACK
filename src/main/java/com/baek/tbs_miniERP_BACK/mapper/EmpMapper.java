package com.baek.tbs_miniERP_BACK.mapper;

import com.baek.tbs_miniERP_BACK.dto.*;
import com.baek.tbs_miniERP_BACK.util.SearchTokens;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmpMapper {

    // 전체 조회
    List<EmpDTO> findAll();
    List<EmpDTO> findAllWithAuth();

    // 전체 조회 (엑셀 추출용)
    List<EmpDTO> selectEmpForExport(@Param("p") EmpFilterParams p, @Param("t") SearchTokens t);

    // 다음 사번 조회 (등록 폼용)
    String nextEmpId();

    // 전체 사번 조회 (등록 폼용)
    List<String> findAllEmpId();

    // 직원 등록
    int createEmps(List<EmpCreateDTO> dtos);

    // 직원 정보 수정
    void updateEmps(List<EmpUpdateDTO> dtos);

    // 직원 정보 수정; 퇴사처리
    void resignEmps(List<EmpResignDTO> dtos);

    // 직위 조회 (정보 수정 등)
    List<String> findAllEmpPos();
}
