package cz.zcu.fav.sportevents.form;

import cz.zcu.fav.sportevents.model.Contestant;

import javax.validation.Valid;


public class AdminSoloRegForm {

    @Valid
    private Contestant contestant;

    private Integer category;

    public Contestant getContestant() {
        return contestant;
    }

    public void setContestant(Contestant contestant) {
        this.contestant = contestant;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
}
