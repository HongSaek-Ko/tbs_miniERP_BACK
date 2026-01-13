package com.baek.tbs_miniERP_BACK.mapper;

import com.baek.tbs_miniERP_BACK.dto.*;
import com.baek.tbs_miniERP_BACK.util.SearchTokens;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AssetHistoryMapper {
    List<AssetHistoryListDTO> findByAssetId(@Param("assetId") String assetId);

    List<AssetHistoryListDTO> findAllHistories();

    // 자산 전체 조회; 엑셀 내보내기
    List<AssetHistoryListDTO> findAllForExport(@Param("p") AssetHisFilterParams p, @Param("t") SearchTokens t);

    int createHistory(@Param("list") List<AssetHisCreDTO> list);
}
