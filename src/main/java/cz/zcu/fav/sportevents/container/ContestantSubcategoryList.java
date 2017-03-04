package cz.zcu.fav.sportevents.container;

import cz.zcu.fav.sportevents.model.ContestantSubcategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HARD on 04.03.2017.
 */
public class ContestantSubcategoryList {

    private List<ContestantSubcategory> contestantSubCategories;

    public ContestantSubcategoryList() {
        this.contestantSubCategories = new ArrayList<>();
    }

    public List<ContestantSubcategory> getContestantSubCategories() {
        return contestantSubCategories;
    }

    public void setContestantSubCategories(List<ContestantSubcategory> contestantSubCategories) {
        this.contestantSubCategories = contestantSubCategories;
    }

    public void add(ContestantSubcategory category){
        this.contestantSubCategories.add(category);
    }

}
