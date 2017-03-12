package cz.zcu.fav.sportevents.form;

import cz.zcu.fav.sportevents.model.User;


public class UserRegistrationForm {

    User user;

    String passwordAgain;

    public User getUser() {
        return user;
    }

    public String getPasswordAgain() {
        return passwordAgain;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }
}
