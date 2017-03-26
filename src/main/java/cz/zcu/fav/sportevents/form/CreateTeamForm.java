package cz.zcu.fav.sportevents.form;


import java.util.List;

public class CreateTeamForm {

    String teamName;

    List<Integer> contestants;

    Integer teamCategory;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<Integer> getContestants() {
        return contestants;
    }

    public void setContestants(List<Integer> contestants) {
        this.contestants = contestants;
    }

    public Integer getTeamCategory() {
        return teamCategory;
    }

    public void setTeamCategory(Integer teamCategory) {
        this.teamCategory = teamCategory;
    }
}
