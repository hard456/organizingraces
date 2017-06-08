package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.model.Race;
import cz.zcu.fav.sportevents.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class EvaluatedRacesController {

    @Autowired
    RaceService raceService;

    /**
     * Zobrazí šablony vyhodnocených závodů.
     * @return
     */
    @RequestMapping(value = "/evaluated_races", method = RequestMethod.GET)
    public ModelAndView evaluatedRaces() {
        ModelAndView model = new ModelAndView();
        List<Race> list;
        list = raceService.getEvaluatedRaces();
        model.setViewName("others/evaluated_races");
        model.addObject("races", list);
        return model;
    }

}
