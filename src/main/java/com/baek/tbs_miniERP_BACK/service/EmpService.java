package com.baek.tbs_miniERP_BACK.service;

import com.baek.tbs_miniERP_BACK.repository.EmpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmpService {
    private final EmpRepository empRepository;
}
