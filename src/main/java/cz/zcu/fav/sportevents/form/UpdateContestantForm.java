package cz.zcu.fav.sportevents.form;


import cz.zcu.fav.sportevents.model.Contestant;

public class UpdateContestantForm {

    Contestant contestant;

    Integer conCategory;

    Integer conId;

    Integer teamCategory;

    public Contestant getContestant() {
        return contestant;
    }

    public void setContestant(Contestant contestant) {
        this.contestant = contestant;
    }

    public Integer getConCategory() {
        return conCategory;
    }

    public void setConCategory(Integer conCategory) {
        this.conCategory = conCategory;
    }

    public Integer getConId() {
        return conId;
    }

    public void setConId(Integer conId) {
        this.conId = conId;
    }

    public Integer getTeamCategory() {
        return teamCategory;
    }

    public void setTeamCategory(Integer teamCategory) {
        this.teamCategory = teamCategory;
    }
}
