package cz.zcu.fav.sportevents.service;

import cz.zcu.fav.sportevents.dao.RaceCooperationDAO;
import cz.zcu.fav.sportevents.model.RaceCooperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RaceCooperationService {

    @Autowired
    RaceCooperationDAO raceCooperationDAO;

    @Transactional
    public List<RaceCooperation> getCooperatorsByRaceId(int race_id){
        return raceCooperationDAO.getCooperatorsByRaceId(race_id);
    }

    @Transactional
    public boolean isUserRaceCooperator(int race_id, int user_id){
        if(raceCooperationDAO.getCooperation(race_id, user_id) != null){
            return true;
        }
        return false;
       }

    @Transactional
    public RaceCooperation getCooperation(int race_id, int user_id){
        return raceCooperationDAO.getCooperation(race_id, user_id);
    }

    @Transactional
    public void saveCooperation(RaceCooperation raceCooperation){
        raceCooperationDAO.save(raceCooperation);
    }

    @Transactional
    public void delete(RaceCooperation cooperation){
        raceCooperationDAO.delete(cooperation);
    }

    @Transactional
    public List<RaceCooperation> getCooperationsByUserId(int userId){
        return raceCooperationDAO.getCooperationsByUserId(userId);
    }

}
