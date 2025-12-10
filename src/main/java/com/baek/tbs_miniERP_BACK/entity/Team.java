package com.baek.tbs_miniERP_BACK.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TEAM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "team_seq_gen")
    @SequenceGenerator(
            name = "team_seq_gen",
            sequenceName = "TEAM_SEQ",
            allocationSize = 1
    )
    @Column(name = "TEAM_ID")
    private Long teamId;

    @Column(name = "TEAM_NAME", length = 100, unique = true)
    private String teamName;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Employee> employees = new ArrayList<>();
}
