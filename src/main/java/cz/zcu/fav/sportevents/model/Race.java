package cz.zcu.fav.sportevents.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "race")
public class Race implements Serializable{

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Column(name = "evaluation", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean evaluation;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "team_size", nullable = false)
    private int teamSize;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isEvaluation() {
        return evaluation;
    }

    public int getUserId() {
        return userId;
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

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
