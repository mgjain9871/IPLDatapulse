package com.example.ipl_dashboard.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.ipl_dashboard.model.Match;
import com.example.ipl_dashboard.model.Team;
import com.example.ipl_dashboard.repository.TeamRepository;
import com.example.ipl_dashboard.repository.MatchRepository;

import javax.transaction.Transactional;

@RestController
@CrossOrigin
@Transactional
public class TeamController {

    private TeamRepository teamRepository;
    private MatchRepository matchRepository;
    
    @Autowired
    public TeamController(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }


    @GetMapping("/team")
    public Iterable<Team> getAllTeam() {
        return this.teamRepository.findAll();
    }

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName) {
        Team team = this.teamRepository.findByTeamName(teamName);
        team.setMatches(matchRepository.findLatestMatchesbyTeam(teamName,4));
            
        return team;
    }

    @GetMapping("/team/{teamName}/matches")
    public List<Match> getMatchesForTeam(@PathVariable String teamName, @RequestParam int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year + 1, 1, 1);
        return this.matchRepository.getMatchesByTeamBetweenDates(
            teamName,
            startDate,
            endDate
            );
    }

    @PostMapping("/team")
    public String addNewTeam(@RequestParam String teamName,@RequestParam int totalMatches, @RequestParam int totalWins, @RequestBody List<Match> matches){
        Team team = new Team();
        team.setTeamName(teamName);
        team.setTotalMatches(totalMatches);
        team.setTotalWins(totalWins);
        team.setMatches(matches);
        this.teamRepository.save(team);
        return "Team added successfully";
    }

    @DeleteMapping("/team/{teamName}")
    public String deleteTeam(@PathVariable String teamName){
        Team team = this.teamRepository.findByTeamName(teamName);
        if(team == null){
            return "Team not found";
        }
        this.teamRepository.deleteByTeamName(teamName);
        return "Team deleted successfully";
    }

    @PutMapping("/team/{teamName}")
    public String updateTeam(@PathVariable String teamName, @RequestParam int totalMatches, @RequestParam int totalWins){
        Team team = this.teamRepository.findByTeamName(teamName);
        if(team == null){
            return "Team not found";
        }
        team.setTotalMatches(totalMatches);
        team.setTotalWins(totalWins);
        this.teamRepository.save(team);
        return "Team updated successfully";
    }
}    