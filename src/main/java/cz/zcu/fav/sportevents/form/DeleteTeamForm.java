package cz.zcu.fav.sportevents.form;

public class DeleteTeamForm {

    Integer teamId;
    Boolean deleteContestants;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Boolean getDeleteContestants() {
        return deleteContestants;
    }

    public void setDeleteContestants(Boolean deleteContestants) {
        this.deleteContestants = deleteContestants;
    }
}
