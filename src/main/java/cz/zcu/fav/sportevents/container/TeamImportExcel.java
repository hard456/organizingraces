package cz.zcu.fav.sportevents.container;

import java.util.ArrayList;
import java.util.List;

public class TeamImportExcel {

    String teamName;
    String solo;
    String category;

    List<ContestantImportExcel> contestants;

    public TeamImportExcel() {
        this.contestants = new ArrayList<>();
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getSolo() {
        return solo;
    }

    public void setSolo(String solo) {
        this.solo = solo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<ContestantImportExcel> getContestants() {
        return contestants;
    }

    public void setContestants(List<ContestantImportExcel> contestants) {
        this.contestants = contestants;
    }

    public void addContestant(ContestantImportExcel contestant){
        contestants.add(contestant);
    }

}
