package com.baek.tbs_miniERP_BACK.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="EMPLOYEE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE,
//            generator = "employee_seq_gen")
//    @SequenceGenerator(
//            name = "employee_seq_gen",
//            sequenceName = "EMPLOYEE_SEQ",
//            allocationSize = 1
//    )
    @Column(name = "EMP_ID")
    private String empId;

    @Column(name = "EMP_NAME", length = 50, nullable = false)
    private String empName;

    @Column(name = "EMP_POS", length = 20)
    private String empPos;

    // EMPLOYEE, LEAVE, RESIGN
    @Column(name = "EMP_STATUS", length = 10, nullable = false)
    @ColumnDefault("'EMPLOYEE'")
    private String empStatus;

    @Column(name = "EMP_REG_DT", nullable = false)
    private LocalDateTime empRegDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Asset> assets = new ArrayList<>();

    @PrePersist
    private void onCreate() {
        if (this.empStatus == null) {
            this.empStatus = "EMPLOYEE";
        }
        if (this.empRegDt == null) {
            this.empRegDt = LocalDateTime.now();
        }
    }
}
