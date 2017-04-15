package cz.zcu.fav.sportevents.model;

import com.sun.istack.internal.Nullable;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

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

    @Transient
    private int penalization;

    @Transient
    private int finalPoints;

    @Column(name = "deadline_time")
    private Integer deadlineTime;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "team_subcat_id")
    private TeamSubcategory category;

    @Column(name = "start_time")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startTime;

    @Column(name = "finish_time")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime finishTime;

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

    public DateTime getFinishTime() {
        return finishTime;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setId(int id) {
        this.id = id;
    }

    public DateTime getStartTime() {
        return startTime;
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

    public int getFinalPoints() {
        return finalPoints;
    }

    public void setFinalPoints(int finalPoints) {
        this.finalPoints = finalPoints;
    }

    public Integer getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(Integer deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(DateTime finishTime) {
        this.finishTime = finishTime;
    }
}
