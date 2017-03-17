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

import java.util.List;

@Controller
public class RaceController {

    @Autowired
    private UserService userService;

    @Autowired
    private RaceService raceService;

    @RequestMapping(value = "/my_races", method = RequestMethod.GET)
    public ModelAndView myRaces() {
        ModelAndView model = new ModelAndView();
        List<Race> list;
        list = raceService.listByUserId(userService.getLoginUser().getId());
        model.setViewName("user/my_races");
        model.addObject("list", list);
        return model;
    }

    @RequestMapping(value = "/avaible_races", method = RequestMethod.GET)
    public ModelAndView avaibleRaces() {
        ModelAndView model = new ModelAndView();
        List<Race> list;
        list = raceService.getRacesToRegistration();
        model.setViewName("others/available_races");
        model.addObject("races", list);
        return model;
    }

    @RequestMapping(value = "/evaluated_races", method = RequestMethod.GET)
    public ModelAndView evaluatedRaces() {
        ModelAndView model = new ModelAndView();
        List<Race> list;
        list = raceService.getEvalutedRaces();
        model.setViewName("others/evaluated_races");
        model.addObject("races", list);
        return model;
    }

    @RequestMapping(value = "/race/{id}", method = RequestMethod.GET)
    public ModelAndView race(@PathVariable("id") int race_id) {
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        if (race == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        } else {
            model.setViewName("race/race");
            model.addObject("user", userService.getLoginUser());
            model.addObject("race", race);
            return model;
        }
    }

}