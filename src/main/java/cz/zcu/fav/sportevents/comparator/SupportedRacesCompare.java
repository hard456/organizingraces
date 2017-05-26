package cz.zcu.fav.sportevents.comparator;

import cz.zcu.fav.sportevents.model.RaceCooperation;

import java.util.Comparator;

public class SupportedRacesCompare implements Comparator<RaceCooperation> {

    @Override
    public int compare(RaceCooperation c1, RaceCooperation c2) {
        if (c1.getRace().getId() < c2.getRace().getId()) {
            return 1;
        }
        return -1;
    }

}
