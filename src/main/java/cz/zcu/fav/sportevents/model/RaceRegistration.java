package cz.zcu.fav.sportevents.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "race_registration")
public class RaceRegistration implements Serializable{

    @Id
    @Column(name = "contestant_id", nullable = false)
    private int contestantId;

    @Id
    @Column(name = "team_id", nullable = false)
    private int teamId;

    public int getContestantId() {
        return contestantId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setContestantId(int contestantId) {
        this.contestantId = contestantId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

}
