package com.baek.tbs_miniERP_BACK.service;

import com.baek.tbs_miniERP_BACK.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssetService {
    private AssetRepository assetRepository;
}
