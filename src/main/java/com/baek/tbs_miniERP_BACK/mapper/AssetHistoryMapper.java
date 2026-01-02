package com.baek.tbs_miniERP_BACK.mapper;

import com.baek.tbs_miniERP_BACK.dto.AssetHisCreDTO;
import com.baek.tbs_miniERP_BACK.dto.AssetHistoryListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AssetHistoryMapper {
    List<AssetHistoryListDTO> findByAssetId(@Param("assetId") String assetId);

    int createHistory(@Param("list") List<AssetHisCreDTO> list);
}
