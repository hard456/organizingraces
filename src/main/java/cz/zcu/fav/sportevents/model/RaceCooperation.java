package cz.zcu.fav.sportevents.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "race_cooperation")
public class RaceCooperation implements Serializable{

    @Id
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Id
    @Column(name = "race_id", nullable = false)
    private int raceId;

    public int getUserId() {
        return userId;
    }

    public int getRaceId() {
        return raceId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }
}
