package cz.zcu.fav.sportevents.service;

import cz.zcu.fav.sportevents.dao.TeamSubcategoryDAO;
import cz.zcu.fav.sportevents.model.TeamSubcategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamSubcategoryService {

    @Autowired
    TeamSubcategoryDAO teamSubcategoryDAO;

    @Transactional
    public List<TeamSubcategory> getListByCategoryId(int category_id){
        return teamSubcategoryDAO.getListByCategoryId(category_id);
    }

    @Transactional
    public void saveList(List<TeamSubcategory> list){
        for (TeamSubcategory item : list) {
            teamSubcategoryDAO.save(item);
        }
    }

    @Transactional
    public TeamSubcategory getSubcategoryById(int id){
        return teamSubcategoryDAO.getSubcategoryById(id);
    }

    @Transactional
    public TeamSubcategory getSubcategoryByNameByCategoryId(String teamCategory, int raceTeamCategory) {
        return teamSubcategoryDAO.getSubcategoryByNameByCategoryId(teamCategory,raceTeamCategory);
    }
}
