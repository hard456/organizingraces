package cz.zcu.fav.sportevents.container;

import cz.zcu.fav.sportevents.model.Contestant;

import java.util.ArrayList;
import java.util.List;

public class ContestantList {

    private List<Contestant> contestants;

    public ContestantList() {
        this.contestants = new ArrayList<>();
    }

    public List<Contestant> getContestants() {
        return contestants;
    }

    public void setContestants(List<Contestant> contestants) {
        this.contestants = contestants;
    }

    public void add(Contestant contestant){
        this.contestants.add(contestant);
    }

}
