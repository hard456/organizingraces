package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.comparator.SupportedRacesCompare;
import cz.zcu.fav.sportevents.model.Race;
import cz.zcu.fav.sportevents.model.RaceCooperation;
import cz.zcu.fav.sportevents.model.User;
import cz.zcu.fav.sportevents.service.RaceCooperationService;
import cz.zcu.fav.sportevents.service.RaceService;
import cz.zcu.fav.sportevents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

@Controller
public class MyRacesController {

    @Autowired
    RaceService raceService;

    @Autowired
    UserService userService;

    @Autowired
    RaceCooperationService raceCooperationService;

    @RequestMapping(value = "/my_races", method = RequestMethod.GET)
    public ModelAndView myRaces() {
        User user = userService.getLoginUser();
        ModelAndView model = new ModelAndView();
        List<Race> myRaces = raceService.listByUserId(user.getId());
        List<RaceCooperation> supportedRaces = raceCooperationService.getCooperationsByUserId(user.getId());
        Collections.sort(supportedRaces, new SupportedRacesCompare());
        model.setViewName("user/my_races");
        model.addObject("myRaces", myRaces);
        model.addObject("supportedRaces", supportedRaces);
        return model;
    }

}
