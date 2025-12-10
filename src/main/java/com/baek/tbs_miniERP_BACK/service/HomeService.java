package com.baek.tbs_miniERP_BACK.service;

import com.baek.tbs_miniERP_BACK.dto.TestDTO;
import com.baek.tbs_miniERP_BACK.entity.Test;
import com.baek.tbs_miniERP_BACK.repository.HomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomeService {
    private final HomeRepository homeRepository;

    public List<Test> findAll() {
        log.info("data? {}", homeRepository.findAll());
        return homeRepository.findAll();
    }
}
