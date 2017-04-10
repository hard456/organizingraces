package cz.zcu.fav.sportevents.container;

public class ContestantImportExcel {

    String firstname;
    String lastname;
    String email;
    String phone;
    String paid;
    String contestantCategory;
    String teamCategory;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getContestantCategory() {
        return contestantCategory;
    }

    public void setContestantCategory(String contestantCategory) {
        this.contestantCategory = contestantCategory;
    }

    public String getTeamCategory() {
        return teamCategory;
    }

    public void setTeamCategory(String teamCategory) {
        this.teamCategory = teamCategory;
    }
}
