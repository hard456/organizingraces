package cz.zcu.fav.sportevents.comparator;

import cz.zcu.fav.sportevents.model.Team;

import java.util.Comparator;

public class FinalPointsCompare implements Comparator<Team> {

    @Override
    public int compare(Team teamOne, Team teamTwo){
        if(teamOne.getFinalPoints() > teamTwo.getFinalPoints()){
            return -1;
        }
        else if(teamOne.getFinalPoints() < teamTwo.getFinalPoints()){
            return 1;
        }
        else{
            return 0;
        }
    }

}
