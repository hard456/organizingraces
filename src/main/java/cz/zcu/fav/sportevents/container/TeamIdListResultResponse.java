package cz.zcu.fav.sportevents.container;

import java.util.ArrayList;
import java.util.List;

public class TeamIdListResultResponse {

    List<Integer> teamIdList;

    String validation;

    public TeamIdListResultResponse() {
        this.teamIdList = new ArrayList<>();
    }

    public List<Integer> getTeamIdList() {
        return teamIdList;
    }

    public void setTeamIdList(List<Integer> teamIdList) {
        this.teamIdList = teamIdList;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String string) {
        this.validation = string;
    }

    public void addToList(Integer number){
        this.teamIdList.add(number);
    }

}
