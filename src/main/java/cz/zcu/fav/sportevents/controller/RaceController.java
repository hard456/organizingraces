package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.container.CooperatorAjaxResponse;
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

@Controller
public class RaceController {

    @Autowired
    private UserService userService;

    @Autowired
    private RaceService raceService;

    @Autowired
    private RaceCooperationService raceCooperationService;

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
            User user = userService.getLoginUser();
            model.setViewName("race/race");
            model.addObject("cooperators", raceCooperationService.getCooperatorsByRaceId(race_id));
            model.addObject("user", user);
            model.addObject("race", race);
            if (user != null) {
                if (raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
                    model.addObject("race_cooperator", true);
                } else {
                    model.addObject("race_cooperator", false);
                }
            }
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

    @RequestMapping(value = "/race/{id}/addCooperator", method = RequestMethod.POST, produces = "application/json")
    public
    @ResponseBody
    CooperatorAjaxResponse addCooperator(HttpServletRequest request, @ModelAttribute("login") String login, @PathVariable("id") int race_id) {
        User user = userService.getLoginUser();
        Race race = raceService.getRaceById(race_id);
        CooperatorAjaxResponse response = new CooperatorAjaxResponse();
        if (race == null || user == null) {
           response.setValidation("fail");
           return response;
        }

        if (!request.getParameterMap().containsKey("login")) {
            response.setValidation("fail");
            return response;
        }

        if (race.getUser().getId() != user.getId()) {
            response.setValidation("fail");
            return response;
        }

        User cooperator = userService.getUser(HtmlUtils.htmlEscape(login));

        if(login.length() < 3 || login.length() > 32){
            response.setValidation("fail");
            return response;
        }

        if (cooperator == null) {
            response.setValidation("wrongname");
            return response;
        }

        if (raceCooperationService.isUserRaceCooperator(race_id, cooperator.getId())) {
            response.setValidation("iscooperator");
            return response;
        }

        if (user.getId() == cooperator.getId()) {
            response.setValidation("owner");
            return response;
        }
        RaceCooperation cooperation = new RaceCooperation();
        cooperation.setRace(race);
        cooperation.setUser(cooperator);
        raceCooperationService.saveCooperation(cooperation);
        response.setValidation("ok");
        response.setUser(cooperator);
        return response;

    }

    @RequestMapping(value = "/race/{id}/deleteCooperator", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody int deleteCooperator(HttpServletRequest request, @ModelAttribute("login") String login, @PathVariable("id") int race_id){
        User user = userService.getLoginUser();
        Race race = raceService.getRaceById(race_id);
        if (race == null || user == null) {
            return -1;
        }

        if (!request.getParameterMap().containsKey("login")) {
            return -1;
        }

        if (race.getUser().getId() != user.getId()) {
            return -1;
        }

        User cooperator = userService.getUser(HtmlUtils.htmlEscape(login));

        if(login.length() < 3 || login.length() > 32){
            return -1;
        }

        if (cooperator == null) {
            return -1;
        }

        RaceCooperation cooperation;
        cooperation = raceCooperationService.getCooperation(race_id, cooperator.getId());

        if (cooperation == null) {
            return -1;
        }

        raceCooperationService.delete(cooperation);

        return cooperator.getId();
    }

    @RequestMapping(value = "/race/{id}/changeRegistration", method = RequestMethod.POST)
    public @ResponseBody int changeRegistration(@PathVariable("id") int race_id){
        User user = userService.getLoginUser();
        Race race = raceService.getRaceById(race_id);

        if (race == null || user == null) {
            return -1;
        }

        if (race.getUser().getId() != user.getId()) {
            return -1;
        }

        if(race.isRegistration()){
            race.setRegistration(false);
            raceService.update(race);
            return 0;
        }
        else{
            race.setRegistration(true);
            raceService.update(race);
            return 1;
        }
    }

    @RequestMapping(value = "/race/{id}/changeEvaluation", method = RequestMethod.POST)
    public @ResponseBody int changeEvaluation(@PathVariable("id") int race_id){
        User user = userService.getLoginUser();
        Race race = raceService.getRaceById(race_id);

        if (race == null || user == null) {
            return -1;
        }

        if (race.getUser().getId() != user.getId()) {
            return -1;
        }

        if(race.isEvaluation()){
            race.setEvaluation(false);
            raceService.update(race);
            return 0;
        }
        else{
            race.setEvaluation(true);
            raceService.update(race);
            return 1;
        }
    }

}