package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.model.Race;
import cz.zcu.fav.sportevents.model.User;
import cz.zcu.fav.sportevents.service.ContestantService;
import cz.zcu.fav.sportevents.service.RaceCooperationService;
import cz.zcu.fav.sportevents.service.RaceService;
import cz.zcu.fav.sportevents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SoloContestantController {

    @Autowired
    RaceService raceService;

    @Autowired
    UserService userService;

    @Autowired
    RaceCooperationService raceCooperationService;

    @Autowired
    ContestantService contestantService;

    @RequestMapping(value = "/race/{id}/contestants/solo", method = RequestMethod.GET)
    public ModelAndView contestants(@PathVariable("id") int race_id) {
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        if (race == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        } else {

            User user;
            if ((user = userService.getLoginUser()) != null) {
                if (raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
                    model.addObject("race_cooperator", true);
                } else {
                    model.addObject("race_cooperator", false);
                }
            }

            model.addObject("race", race);
            model.addObject("user", user);
            model.addObject("contestants", contestantService.getSoloContestants(race_id));
            model.setViewName("race/contestants_solo");
            return model;
        }
    }

}
