package cz.zcu.fav.sportevents.service;

import cz.zcu.fav.sportevents.dao.TeamCategoryDAO;
import cz.zcu.fav.sportevents.model.ContestantCategory;
import cz.zcu.fav.sportevents.model.TeamCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamCategoryService {

    @Autowired
    TeamCategoryDAO teamCategoryDAO;

    @Transactional
    public List<ContestantCategory> getDefaultCategories(){
        return teamCategoryDAO.getDefaultCategories();
    }

    @Transactional
    public void save(TeamCategory teamCategory){
        teamCategoryDAO.save(teamCategory);
    }

    @Transactional
    public boolean exists(int id){
        if(teamCategoryDAO.getCategoryById(id) != null){
            return true;
        }
        return false;
    }

    @Transactional
    public TeamCategory getCategoryById(int id){
        return teamCategoryDAO.getCategoryById(id);
    }

}
