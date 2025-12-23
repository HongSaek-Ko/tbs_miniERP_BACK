package com.baek.tbs_miniERP_BACK.repository;

import com.baek.tbs_miniERP_BACK.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {
    Team findByTeamName(String teamName);
}
