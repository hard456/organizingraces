package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.model.Contestant;
import cz.zcu.fav.sportevents.model.Race;
import cz.zcu.fav.sportevents.model.User;
import cz.zcu.fav.sportevents.service.ContestantService;
import cz.zcu.fav.sportevents.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class RaceController {

    @Autowired
    UserController userController;

    @Autowired
    RaceService raceService;

    @Autowired
    ContestantService contestantService;

    @RequestMapping(value = "/create_race",method = RequestMethod.GET)
    public String createRace() {
        return "create_race";
    }

    @RequestMapping(value = "/my_races",method = RequestMethod.GET)
    public ModelAndView myRaces() {
        ModelAndView model = new ModelAndView();
        List<Race> list;
        list = raceService.listByUserId(userController.getUser().getId());
        model.setViewName("my_races");
        model.addObject("list",list);
        return model;
    }

    @RequestMapping(value = "/create_event",method = RequestMethod.POST)
    public ModelAndView createEvent(@RequestParam("name") String name) {

        ModelAndView model = new ModelAndView();
        model.setViewName("race_create_result");

        Race race = new Race();
        race.setName(name);
        race.setUserId(userController.getUser().getId());
        race.setEvaluation(false);

        if(raceService.isExistRaceByUserId(race.getUserId(),name)){
            model.addObject("result","The race already exists.");
        }
        else{
            raceService.createRace(race);
            model.addObject("result","The race was created.");
        }
        return model;
    }

    @RequestMapping(value = "/race/{id}", method = RequestMethod.GET)
    public ModelAndView race(@PathVariable("id") int race_id){
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        if(race == null){
            model.addObject("error","404");
            model.setViewName("error/error_page");
            return model;
        }
        else{
            model.setViewName("race");
            model.addObject("user",userController.getUser());
            model.addObject("race",race);
            return model;
        }
    }

    @RequestMapping(value = "/race/{id}/results", method = RequestMethod.GET)
    public ModelAndView results(@PathVariable("id") int race_id){
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        if(race == null){
            model.addObject("error","404");
            model.setViewName("error/error_page");
            return model;
        }
        else {
            if(race.isEvaluation()){
                model.addObject("race",race);
                model.addObject("user",userController.getUser());
                model.setViewName("results");
                return model;
            }
            else{
                model.addObject("error","401");
                model.setViewName("error/error_page");
                return model;
            }
        }
    }

    @RequestMapping(value = "/race/{id}/contestants/solo", method = RequestMethod.GET)
    public ModelAndView contestants(@PathVariable("id") int race_id){
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        if(race == null){
            model.addObject("error","404");
            model.setViewName("error/error_page");
            return model;
        }
        else {
            model.addObject("race",race);
            model.addObject("user",userController.getUser());
            model.addObject("contestants",contestantService.getSoloContestants(race_id));
            model.setViewName("contestants_solo");
            return model;
        }
    }

    @RequestMapping(value = "/race/{id}/contestants/teams", method = RequestMethod.GET)
    public ModelAndView teams(@PathVariable("id") int race_id){
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        if(race == null){
            model.addObject("error","404");
            model.setViewName("error/error_page");
            return model;
        }
        else {
            model.addObject("race",race);
            model.addObject("user",userController.getUser());
            model.setViewName("teams");
            return model;
        }
    }

    @RequestMapping(value = "/race/{id}/contestants/full_list", method = RequestMethod.GET)
    public ModelAndView contestants_list(@PathVariable("id") int race_id){
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        if(race == null){
            model.addObject("error","404");
            model.setViewName("error/error_page");
            return model;
        }
        else {
            model.addObject("race",race);
            model.addObject("user",userController.getUser());
            model.setViewName("contestants_list");
            return model;
        }
    }

    @RequestMapping(value = "/race/{id}/registration", method = RequestMethod.GET)
    public ModelAndView race_registration(@PathVariable("id") int race_id){
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        if(race == null){
            model.addObject("error","404");
            model.setViewName("error/error_page");
            return model;
        }
        else {
            model.addObject("race",race);
            model.addObject("user",userController.getUser());
            model.setViewName("race_registration");
            return model;
        }

    }

    @RequestMapping(value = "/race/{id}/addSoloContestant", method = RequestMethod.POST)
    public ModelAndView addSoloContestant(@RequestParam("category") String category, @PathVariable("id") int race_id){

        Contestant contestant;
        contestant = userToContestant(userController.getUser());
        contestant.setPaid(false);
        contestant.setCategory(category);
        contestant.setRaceId(race_id);
        contestantService.saveContestant(contestant);

        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);

        if(race == null){
            model.addObject("error","404");
            model.setViewName("error/error_page");
            return model;
        }
        else {
            model.addObject("race",race);
            model.addObject("user",userController.getUser());
            model.addObject("result","Registration completed successfully.");
            model.setViewName("race_registration");
            return model;
        }

    }

    private Contestant userToContestant(User user){
        Contestant contestant = new Contestant();
        contestant.setEmail(user.getEmail());
        contestant.setFirstname(user.getFirstname());
        contestant.setLastname(user.getSurname());
        return contestant;
    }

}