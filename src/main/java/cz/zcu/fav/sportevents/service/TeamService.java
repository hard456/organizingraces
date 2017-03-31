package cz.zcu.fav.sportevents.service;

import cz.zcu.fav.sportevents.dao.TeamDAO;
import cz.zcu.fav.sportevents.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    TeamDAO teamDAO;

    @Transactional
    public void save(Team team){
        teamDAO.save(team);
    }

    @Transactional
    public List<Team> getTeamsByRaceId(int race_id){
        return teamDAO.getTeamsByRaceId(race_id);
    }

    @Transactional
    public Team getTeamById(int id){
        return teamDAO.getTeamById(id);
    }

    @Transactional
    public void delete(Team team){
        teamDAO.delete(team);
    }

}
