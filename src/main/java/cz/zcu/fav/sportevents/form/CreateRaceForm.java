package cz.zcu.fav.sportevents.form;


import cz.zcu.fav.sportevents.model.ContestantSubcategory;
import cz.zcu.fav.sportevents.model.Race;
import cz.zcu.fav.sportevents.model.TeamSubcategory;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

public class CreateRaceForm {

    @Valid
    private List<ContestantSubcategory> contestantSubCategories;

    @Valid
    private List<TeamSubcategory> teamSubCategories;

    @Pattern(regexp = "none|defaultValue|own")
    private String conRadio;

    @Pattern(regexp = "none|defaultValue|own")
    private String teamRadio;

    @Valid
    private Race race;

    @NumberFormat
    private Integer defTeamCategoryId;

    @NumberFormat
    private Integer defConCategoryId;

    public CreateRaceForm() {
        this.contestantSubCategories = new ArrayList<>();
        this.teamSubCategories = new ArrayList<>();
    }

    public String getConRadio() {
        return conRadio;
    }

    public void setConRadio(String conRadio) {
        this.conRadio = conRadio;
    }

    public void setTeamRadio(String teamRadio) {
        this.teamRadio = teamRadio;
    }

    public List<ContestantSubcategory> getContestantSubCategories() {
        return contestantSubCategories;
    }

    public void setContestantSubCategories(List<ContestantSubcategory> contestantSubCategories) {
        this.contestantSubCategories = contestantSubCategories;
    }

    public void add(ContestantSubcategory category){
        this.contestantSubCategories.add(category);
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

    public String getTeamRadio() {
        return teamRadio;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Integer getDefTeamCategoryId() {
        return defTeamCategoryId;
    }

    public Integer getDefConCategoryId() {
        return defConCategoryId;
    }

    public void setDefTeamCategoryId(Integer defTeamCategoryId) {
        this.defTeamCategoryId = defTeamCategoryId;
    }

    public void setDefConCategoryId(Integer defConCategoryId) {
        this.defConCategoryId = defConCategoryId;
    }

}
