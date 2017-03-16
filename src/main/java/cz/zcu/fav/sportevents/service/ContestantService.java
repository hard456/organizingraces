package cz.zcu.fav.sportevents.service;

import cz.zcu.fav.sportevents.dao.ContestantDAO;
import cz.zcu.fav.sportevents.model.Contestant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContestantService {

    @Autowired
    ContestantDAO contestantDAO;

    @Transactional
    public void saveContestant(Contestant contestant){
        contestantDAO.save(contestant);
    }

    @Transactional
    public List<Contestant> getSoloContestants(int race_id){
        return contestantDAO.getSoloContestants(race_id);
    }

    @Transactional
    public List<Contestant> getContestantsByRaceId(int race_id){
        return contestantDAO.getContestantsByRaceId(race_id);
    }

    @Transactional
    public List<Contestant> getListByUserAndRaceId(int user_id, int race_id){
        return contestantDAO.getListByUserId(user_id, race_id);
    }

}
