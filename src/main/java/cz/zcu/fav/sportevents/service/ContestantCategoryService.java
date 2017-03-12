package cz.zcu.fav.sportevents.service;

import cz.zcu.fav.sportevents.dao.ContestantCategoryDAO;
import cz.zcu.fav.sportevents.model.ContestantCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContestantCategoryService {

    @Autowired
    ContestantCategoryDAO contestantCategoryDAO;

    @Transactional
    public List<ContestantCategory> getDefaultCategories(){
        return contestantCategoryDAO.getDefaultCategories();
    }

    @Transactional
    public boolean exist(int id){
        if(contestantCategoryDAO.getCategoryById(id) != null){
            return true;
        }
        return false;
    }

    @Transactional
    public ContestantCategory getCategoryById(int id){
        return contestantCategoryDAO.getCategoryById(id);
    }

    @Transactional
    public void save(ContestantCategory contestantCategory){
        contestantCategoryDAO.save(contestantCategory);
    }

}
