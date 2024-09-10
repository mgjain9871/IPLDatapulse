package com.example.ipl_dashboard.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.ipl_dashboard.model.Team;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long>{

	Team findByTeamName(String teamName);
	void deleteByTeamName(String teamName);
}