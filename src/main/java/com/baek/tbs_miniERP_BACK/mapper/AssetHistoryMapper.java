package com.baek.tbs_miniERP_BACK.mapper;

import com.baek.tbs_miniERP_BACK.dto.AssetHistoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AssetHistoryMapper {
    List<AssetHistoryDTO> findByAssetId(@Param("assetId") String assetId);

    int insertHistory(AssetHistoryDTO dto);
}
