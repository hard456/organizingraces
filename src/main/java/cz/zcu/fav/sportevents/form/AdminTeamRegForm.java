package cz.zcu.fav.sportevents.form;

import com.sun.istack.internal.Nullable;
import cz.zcu.fav.sportevents.model.Contestant;

import javax.validation.Valid;
import java.util.List;

public class AdminTeamRegForm {

    @Valid
    private List<Contestant> contestants;

    @Nullable
    private List<Integer> teammateCategory;

    private String teamName;

    @Nullable
    private Integer teamCategory;

    public List<Contestant> getContestants() {
        return contestants;
    }

    public void setContestants(List<Contestant> contestants) {
        this.contestants = contestants;
    }

    public List<Integer> getTeammateCategory() {
        return teammateCategory;
    }

    public void setTeammateCategory(List<Integer> teammateCategory) {
        this.teammateCategory = teammateCategory;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getTeamCategory() {
        return teamCategory;
    }

    public void setTeamCategory(Integer teamCategory) {
        this.teamCategory = teamCategory;
    }
}
