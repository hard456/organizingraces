package cz.zcu.fav.sportevents.container;

import java.util.List;

public class CreateTeamAjaxResponse {

    String validation;

    List<Integer> contestantId;

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public List<Integer> getContestantId() {
        return contestantId;
    }

    public void setContestantId(List<Integer> contestantId) {
        this.contestantId = contestantId;
    }
}
