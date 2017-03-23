
package cz.zcu.fav.sportevents.service;

import cz.zcu.fav.sportevents.dao.RaceDAO;
import cz.zcu.fav.sportevents.model.Race;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RaceService {

    @Autowired
    RaceDAO raceDAO;

    @Transactional
    public void save(Race race){
        raceDAO.save(race);
    }

    @Transactional
    public List<Race> listByUserId(int id){
        return raceDAO.listByUserId(id);
    }

    @Transactional
    public boolean isExistRaceByName(String name){
        if(raceDAO.getRaceByUserId(name) == null){
            return false;
        }
        return true;
    }

    @Transactional
    public List<Race> getRacesToRegistration(){
        return raceDAO.getRacesToRegistration();
    }

    @Transactional
    public Race getRaceById(int id){
        return raceDAO.getRaceById(id);
    }

    @Transactional
    public List<Race> getEvalutedRaces(){
        return raceDAO.getEvalutedRaces();
    }

    @Transactional
    public void deleteRace(Race race){
        raceDAO.delete(race);
    }

}
