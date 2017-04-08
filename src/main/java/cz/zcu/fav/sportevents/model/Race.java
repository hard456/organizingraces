package cz.zcu.fav.sportevents.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "race")
public class Race implements Serializable{

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @NotNull
    @Size(min = 3, max = 32)
    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Column(name = "evaluation", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean evaluation;

    @Column(name = "registration", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean registration;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Min(1)
    @Max(5)
    @Column(name = "team_size", nullable = false)
    private Integer teamSize;

    @ManyToOne
    @JoinColumn(name = "con_category_id")
    private ContestantCategory contestantCategory;

    @ManyToOne
    @JoinColumn(name = "team_category_id")
    private TeamCategory teamCategory;

    public ContestantCategory getContestantCategory() {
        return contestantCategory;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isEvaluation() {
        return evaluation;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEvaluation(boolean evaluation) {
        this.evaluation = evaluation;
    }

    public User getUser() {
        return user;
    }

    public TeamCategory getTeamCategory() {
        return teamCategory;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTeamCategory(TeamCategory teamCategory) {
        this.teamCategory = teamCategory;
    }

    public void setContestantCategory(ContestantCategory contestantCategory) {
        this.contestantCategory = contestantCategory;
    }

    public void setTeamSize(Integer teamSize) {
        this.teamSize = teamSize;
    }

    public boolean isRegistration() {
        return registration;
    }

    public void setRegistration(boolean registration) {
        this.registration = registration;
    }

    public void setId(int id) {
        this.id = id;
    }
}
