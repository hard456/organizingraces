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
    public void createRace(Race race){
        raceDAO.save(race);
    }

    @Transactional
    public List<Race> listByUserId(int id){
        return raceDAO.listByUserId(id);
    }

    @Transactional
    public boolean isExistRaceByUserId(int id, String name){
        if(raceDAO.getRaceByUserId(id,name) == null){
            return false;
        }
        return true;
    }

    @Transactional
    public Race getRaceById(int id){
        return raceDAO.getRaceById(id);
    }

}
