package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.model.Race;
import cz.zcu.fav.sportevents.model.RaceCooperation;
import cz.zcu.fav.sportevents.model.User;
import cz.zcu.fav.sportevents.service.RaceCooperationService;
import cz.zcu.fav.sportevents.service.RaceService;
import cz.zcu.fav.sportevents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class RaceController {

    @Autowired
    private UserService userService;

    @Autowired
    private RaceService raceService;

    @Autowired
    private RaceCooperationService raceCooperationService;

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

    @RequestMapping(value = "/race/{id}/delete", method = RequestMethod.GET)
    public ModelAndView deleteRaceView(@PathVariable("id") int race_id) {
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        if (race == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        } else {
            model.setViewName("race/delete");
            model.addObject("user", userService.getLoginUser());
            model.addObject("race", race);
            return model;
        }
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

    @RequestMapping(value = "/race/{id}/deleteRace", method = RequestMethod.POST)
    public ModelAndView deleteRace(HttpServletRequest request, @ModelAttribute(value = "password") String password, @PathVariable("id") int race_id) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/race/delete_result");
        User user = userService.getLoginUser();
        Race race = raceService.getRaceById(race_id);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (race == null || user == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        }

        if (!request.getParameterMap().containsKey("password")) {
            model.addObject("invalid", true);
            model.addObject("result", "Something went wrong");
            model.addObject("user", user);
            model.addObject("race", race);
            return model;
        }

        if (race.getUser().getId() == user.getId()) {
            if (encoder.matches(password, user.getPassword())) {
                raceService.deleteRace(race);
                model.addObject("invalid", false);
                model.addObject("result", "The race was deleted");
                return model;
            } else {
                model.addObject("invalid", true);
                model.addObject("result", "Wrong password");
                model.addObject("user", user);
                model.addObject("race", race);
                return model;
            }
        }
        model.addObject("invalid", true);
        model.addObject("result", "You are not allowed to do that.");
        model.addObject("user", user);
        model.addObject("race", race);
        return model;

    }

    @RequestMapping(value = "/race/{id}/addCooperator", method = RequestMethod.POST)
    public
    @ResponseBody
    String addCooperator(HttpServletRequest request, @ModelAttribute("login") String login, @PathVariable("id") int race_id) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/race/add_cooperator_result");
        User user = userService.getLoginUser();
        Race race = raceService.getRaceById(race_id);

        if (race == null || user == null) {
            return "fail";
        }

        if (!request.getParameterMap().containsKey("login")) {
            return "fail";
        }

        if (race.getUser().getId() != user.getId()) {
            return "fail";
        }

        User cooperator = userService.getUser(HtmlUtils.htmlEscape(login));
        if (cooperator == null) {
            return "wrongname";
        }

        if (raceCooperationService.isUserRaceCooperator(race_id, cooperator.getId())) {
            return "iscooperator";
        }

        if (user.getId() == cooperator.getId()) {
            return "owner";
        }
        RaceCooperation cooperation = new RaceCooperation();
        cooperation.setRace(race);
        cooperation.setUser(cooperator);
        raceCooperationService.saveCooperation(cooperation);
        return "ok";


    }

}