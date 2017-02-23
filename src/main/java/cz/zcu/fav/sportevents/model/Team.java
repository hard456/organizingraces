package cz.zcu.fav.sportevents.model;

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

    @Column(name = "category", length = 32)
    private String category;

    @Column(name = "start_time", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "finish_time", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishTime;

    @Column(name = "race_id", nullable = false)
    private int raceId;

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

    public String getCategory() {
        return category;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public int getRaceId() {
        return raceId;
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

}
