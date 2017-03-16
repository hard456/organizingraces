package cz.zcu.fav.sportevents.service;

import cz.zcu.fav.sportevents.dao.TeamDAO;
import cz.zcu.fav.sportevents.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamService {

    @Autowired
    TeamDAO teamDAO;

    @Transactional
    public void save(Team team){
        teamDAO.save(team);
    }

}
