package cz.zcu.fav.sportevents.container;

import cz.zcu.fav.sportevents.model.TeamSubcategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HARD on 04.03.2017.
 */
public class TeamSubcategoryList {

    private List<TeamSubcategory> teamSubCategories;

    public TeamSubcategoryList() {
        this.teamSubCategories = new ArrayList<>();
    }

    public List<TeamSubcategory> getTeamSubCategories() {
        return teamSubCategories;
    }

    public void setTeamSubCategories(List<TeamSubcategory> teamSubCategories) {
        this.teamSubCategories = teamSubCategories;
    }

    public void add(TeamSubcategory category){
        this.teamSubCategories.add(category);
    }

}
