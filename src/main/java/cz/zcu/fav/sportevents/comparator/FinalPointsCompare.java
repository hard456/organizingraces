package cz.zcu.fav.sportevents.comparator;

import cz.zcu.fav.sportevents.model.Team;
import org.joda.time.Duration;

import java.util.Comparator;

public class FinalPointsCompare implements Comparator<Team> {

    /**
     * Pro seřazení listu týmů podle finálních bodů. Při shodě bodů dojde k řazení podle času, za který tým uběhl závod.
     * @param teamOne
     * @param teamTwo
     * @return -1 pro lepší skóre teamOne, 1 pro lepší skóre teamTwo, 0 pro shoda
     */
    @Override
    public int compare(Team teamOne, Team teamTwo) {
        if (teamOne.getFinalPoints() > teamTwo.getFinalPoints()) {
            return -1;
        } else if (teamOne.getFinalPoints() < teamTwo.getFinalPoints()) {
            return 1;
        } else {
            if (teamOne.getStartTime() != null && teamOne.getFinishTime() != null &&
                    teamTwo.getStartTime() != null && teamTwo.getFinishTime() != null) {

                Duration durationOne = new Duration(teamOne.getStartTime(), teamOne.getFinishTime());
                Duration durationTwo = new Duration(teamTwo.getStartTime(), teamTwo.getFinishTime());

                if (durationOne.getMillis() < durationTwo.getMillis()) {
                    return -1;
                } else if (durationOne.getMillis() > durationTwo.getMillis()) {
                    return 1;
                }

            }
            return 0;
        }
    }

}
