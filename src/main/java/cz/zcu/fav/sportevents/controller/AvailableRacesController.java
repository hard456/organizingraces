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
public class AvailableRacesController {

    @Autowired
    RaceService raceService;

    @RequestMapping(value = "/avaible_races", method = RequestMethod.GET)
    public ModelAndView avaibleRaces() {
        ModelAndView model = new ModelAndView();
        List<Race> list;
        list = raceService.getRacesToRegistration();
        model.setViewName("others/available_races");
        model.addObject("races", list);
        return model;
    }

}
