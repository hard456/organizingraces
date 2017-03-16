package cz.zcu.fav.sportevents.model;

import com.sun.istack.internal.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "team")
public class Team implements Serializable{

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "name", length = 32)
    private String name;

    @Column(name = "points")
    private int points;

    @Column(name = "bonus")
    private int bonus;

    @Column(name = "penalization")
    private int penalization;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "team_subcat_id")
    private TeamSubcategory category;

    @Column(name = "start_time", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "finish_time", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishTime;

    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getBonus() {
        return bonus;
    }

    public int getPenalization() {
        return penalization;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public void setPenalization(int penalization) {
        this.penalization = penalization;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public TeamSubcategory getCategory() {
        return category;
    }

    public void setCategory(TeamSubcategory category) {
        this.category = category;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}
