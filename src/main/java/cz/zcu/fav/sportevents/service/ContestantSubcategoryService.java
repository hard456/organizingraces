package cz.zcu.fav.sportevents.service;

import cz.zcu.fav.sportevents.dao.ContestantSubcategoryDAO;
import cz.zcu.fav.sportevents.model.ContestantSubcategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContestantSubcategoryService {

    @Autowired
    ContestantSubcategoryDAO contestantSubcategoryDAO;

    @Transactional
    public List<ContestantSubcategory> getListByCategoryId(int category_id) {
        return contestantSubcategoryDAO.getListByCategoryId(category_id);
    }

    @Transactional
    public void saveList(List<ContestantSubcategory> list){
        for (ContestantSubcategory item : list) {
            contestantSubcategoryDAO.save(item);
        }
    }

    @Transactional
    public ContestantSubcategory getSubCategoryById(int id){
        return contestantSubcategoryDAO.getSubCategoryById(id);
    }

    @Transactional
    public ContestantSubcategory getCategoryWithRaceId(int category, int race_category){
        return contestantSubcategoryDAO.getByIdAndRaceId(category,race_category);
    }

    public ContestantSubcategory getSubcategoryByName(String contestantCategory, int raceConCategory) {
        return contestantSubcategoryDAO.getSubcategoryByName(contestantCategory,raceConCategory);
    }
}
