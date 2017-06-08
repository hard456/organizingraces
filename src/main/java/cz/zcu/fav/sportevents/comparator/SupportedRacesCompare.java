package cz.zcu.fav.sportevents.comparator;

import cz.zcu.fav.sportevents.model.RaceCooperation;

import java.util.Comparator;

public class SupportedRacesCompare implements Comparator<RaceCooperation> {

    /**
     * Pro seřazení listu kooperací dle ID závodu.
     * @param c1
     * @param c2
     * @return vrací kooperaci s nižším ID, 1 pro c1, -1 pro c2
     */
    @Override
    public int compare(RaceCooperation c1, RaceCooperation c2) {
        if (c1.getRace().getId() < c2.getRace().getId()) {
            return 1;
        }
        return -1;
    }

}
