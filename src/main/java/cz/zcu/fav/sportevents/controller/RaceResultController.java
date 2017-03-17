package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.model.Race;
import cz.zcu.fav.sportevents.service.RaceService;
import cz.zcu.fav.sportevents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RaceResultController {

    @Autowired
    RaceService raceService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/race/{id}/results", method = RequestMethod.GET)
    public ModelAndView results(@PathVariable("id") int race_id) {
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        if (race == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        } else {
            if (race.isEvaluation()) {
                model.addObject("race", race);
                model.addObject("user", userService.getLoginUser());
                model.setViewName("race/results");
                return model;
            } else {
                model.addObject("error", "401");
                model.setViewName("error/error_page");
                return model;
            }
        }
    }

}
