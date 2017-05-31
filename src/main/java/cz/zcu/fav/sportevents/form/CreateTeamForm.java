package cz.zcu.fav.sportevents.form;


import java.util.List;

public class CreateTeamForm {

    String teamName;

    List<String> contestants;

    String teamCategory;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<String> getContestants() {
        return contestants;
    }

    public void setContestants(List<String> contestants) {
        this.contestants = contestants;
    }

    public String getTeamCategory() {
        return teamCategory;
    }

    public void setTeamCategory(String teamCategory) {
        this.teamCategory = teamCategory;
    }
}
