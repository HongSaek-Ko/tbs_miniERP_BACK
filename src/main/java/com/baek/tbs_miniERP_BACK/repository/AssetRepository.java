package com.baek.tbs_miniERP_BACK.repository;

import com.baek.tbs_miniERP_BACK.dto.AssetListDTO;
import com.baek.tbs_miniERP_BACK.entity.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    @Query(
            value = """
            select new com.baek.tbs_miniERP_BACK.dto.AssetListDTO(
                a.assetId,
                a.assetType,
                a.assetManufacturer,
                a.assetModelName,
                a.assetSn,
                a.assetManufacturedAt,
                e.empName,
                e.empPos,
                t.teamName,
                a.assetLoc,
                a.assetIssuanceDate,
                a.assetDesc
            )
            from Asset a
            left join a.employee e
            left join e.team t
            where (:assetType is null or a.assetType = :assetType)
              and (:empName is null 
                   or lower(e.empName) like lower(concat('%', :empName, '%')))
              and (:empPos is null or e.empPos = :empPos)
        """,
            countQuery = """
            select count(a)
            from Asset a
            left join a.employee e
            left join e.team t
            where (:assetType is null or a.assetType = :assetType)
              and (:empName is null 
                   or lower(e.empName) like lower(concat('%', :empName, '%')))
              and (:empPos is null or e.empPos = :empPos)
        """
    )
    Page<AssetListDTO> getAssetList(
            @Param("assetType") String assetType,
            @Param("empName") String empName,
            @Param("empPos") String empPos,
            Pageable pageable
    );
}
