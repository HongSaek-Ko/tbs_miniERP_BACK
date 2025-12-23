package com.baek.tbs_miniERP_BACK.repository;

import com.baek.tbs_miniERP_BACK.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpRepository extends JpaRepository<Employee, String>, JpaSpecificationExecutor<Employee> {

    @Query("""
        SELECT e
        FROM Employee e
        WHERE e.empId = :empId
        """
    )
    Employee findByEmpIdForUpdate(@Param("empId") String empId);
}
