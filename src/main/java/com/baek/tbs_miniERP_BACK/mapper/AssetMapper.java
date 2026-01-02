package com.baek.tbs_miniERP_BACK.mapper;

import com.baek.tbs_miniERP_BACK.dto.*;
import com.baek.tbs_miniERP_BACK.util.SearchTokens;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AssetMapper {
    // 자산 단일 조회
    AssetDTO findAssetByAssetId(String assetId);

    // 자산 전체 조회
    List<AssetListDTO> findAll(String assetStatus);

    // 자산 전체 조회; 엑셀 내보내기
    List<AssetListDTO> findAllForExport(@Param("p")AssetFilterParams p, @Param("t")SearchTokens t);

    // 자산 다음 번호 조회 (등록용)
    String findMaxIdByAssetType(String assetType);

    // 신규 자산 정보 등록
    int createAssets(@Param("list") List<AssetCreateDTO> dtos);

    // 자산 정보 수정
    void updateAssets(List<AssetUpdateDTO> dtos);

    // 자산 정보 수정; 폐기 처리
    void disposeAssets(List<AssetDisposeDTO> dtos);

    List<AssetSnapshotDTO> findSnapshotByAssetId(@Param("list") List<String> list);



}
