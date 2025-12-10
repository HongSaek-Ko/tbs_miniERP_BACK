package com.baek.tbs_miniERP_BACK.repository;

import com.baek.tbs_miniERP_BACK.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpRepository extends JpaRepository<Employee, Long> {
}
