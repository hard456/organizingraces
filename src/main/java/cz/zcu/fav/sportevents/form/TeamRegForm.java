package cz.zcu.fav.sportevents.form;

import cz.zcu.fav.sportevents.model.Contestant;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class TeamRegForm {

    @Valid
    private List<Contestant> contestants;

    @Size(min = 3, max = 32)
    private String teamName;

    private Integer teamCategory;

    private Integer conCategory;

    private List<Integer> teammateCategory;

    public TeamRegForm() {
        this.contestants = new ArrayList<>();
    }

    public List<Contestant> getContestants() {
        return contestants;
    }

    public String getTeamName() {
        return teamName;
    }

    public Integer getTeamCategory() {
        return teamCategory;
    }

    public Integer getConCategory() {
        return conCategory;
    }

    public void setContestants(List<Contestant> contestants) {
        this.contestants = contestants;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setTeamCategory(Integer teamCategory) {
        this.teamCategory = teamCategory;
    }

    public void setConCategory(Integer conCategory) {
        this.conCategory = conCategory;
    }

    public List<Integer> getTeammateCategory() {
        return teammateCategory;
    }

    public void setTeammateCategory(List<Integer> teammateCategory) {
        this.teammateCategory = teammateCategory;
    }
}
