package cz.zcu.fav.sportevents.container;

import cz.zcu.fav.sportevents.model.User;

public class CooperatorAjaxResponse {

    private User user;

    private String validation;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }
}
