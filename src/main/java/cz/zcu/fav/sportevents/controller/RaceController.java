package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.container.ContestantList;
import cz.zcu.fav.sportevents.model.Contestant;
import cz.zcu.fav.sportevents.model.Race;
import cz.zcu.fav.sportevents.model.User;
import cz.zcu.fav.sportevents.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    ContestantCategoryService contestantCategoryService;

    @Autowired
    TeamCategoryService teamCategoryService;

    @Autowired
    RaceCooperationService raceCooperationService;

    @RequestMapping(value = "/create_race", method = RequestMethod.GET)
    public ModelAndView createRace() {
        ModelAndView model = new ModelAndView();
        model.setViewName("create_race");
        model.addObject("team_categories", teamCategoryService.getDefaultCategories());
        model.addObject("con_categories", contestantCategoryService.getDefaultCategories());
        return model;
    }

    @RequestMapping(value = "/my_races", method = RequestMethod.GET)
    public ModelAndView myRaces() {
        ModelAndView model = new ModelAndView();
        List<Race> list;
        list = raceService.listByUserId(userController.getUser().getId());
        model.setViewName("my_races");
        model.addObject("list", list);
        return model;
    }

    @RequestMapping(value = "/create_event", method = RequestMethod.POST)
    public ModelAndView createEvent(Race race) {

        ModelAndView model = new ModelAndView();
        model.setViewName("race_create_result");


        race.setUserId(userController.getUser().getId());
        race.setEvaluation(false);

        if (raceService.isExistRaceByUserId(race.getUserId(), race.getName())) {
            model.addObject("result", "The race already exists.");
        } else {
            raceService.createRace(race);
            model.addObject("result", "The race was created.");
        }
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
            model.setViewName("race");
            model.addObject("user", userController.getUser());
            model.addObject("race", race);
            return model;
        }
    }

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
                model.addObject("user", userController.getUser());
                model.setViewName("results");
                return model;
            } else {
                model.addObject("error", "401");
                model.setViewName("error/error_page");
                return model;
            }
        }
    }

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
            if((user = userController.getUser()) != null){
                if(raceCooperationService.isUserRaceCooperator(race_id,user.getId())){
                    model.addObject("race_cooperator",true);
                }
                else{
                    model.addObject("race_cooperator",false);
                }
            }

            model.addObject("race", race);
            model.addObject("user", user);
            model.addObject("contestants", contestantService.getSoloContestants(race_id));
            model.setViewName("contestants_solo");
            return model;
        }
    }

    @RequestMapping(value = "/race/{id}/contestants/teams", method = RequestMethod.GET)
    public ModelAndView teams(@PathVariable("id") int race_id) {
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        if (race == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        } else {
            model.addObject("race", race);
            model.addObject("user", userController.getUser());
            model.setViewName("teams");
            return model;
        }
    }

    @RequestMapping(value = "/race/{id}/contestants/full_list", method = RequestMethod.GET)
    public ModelAndView contestants_list(@PathVariable("id") int race_id) {
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);

        if (race == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        } else {

            User user;
            if((user = userController.getUser()) != null){
                if(raceCooperationService.isUserRaceCooperator(race_id,user.getId())){
                    model.addObject("race_cooperator",true);
                }
                else{
                    model.addObject("race_cooperator",false);
                }
            }

            model.addObject("contestants", contestantService.getContestantsByRaceId(race_id));
            model.addObject("race", race);
            model.addObject("user", user);

            model.setViewName("contestants_list");
            return model;
        }
    }

    @RequestMapping(value = "/race/{id}/registration", method = RequestMethod.GET)
    public ModelAndView race_registration(@PathVariable("id") int race_id) {
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        ContestantList contestantList;

        if (race == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        } else {

            User user;
            if((user = userController.getUser()) != null){
                if(raceCooperationService.isUserRaceCooperator(race_id,user.getId())){
                    model.addObject("race_cooperator",true);
                }
                else{
                    model.addObject("race_cooperator",false);
                }
            }

            model.addObject("race", race);
            model.addObject("user", user);

            contestantList = new ContestantList();

            for (int i = 0; i < race.getTeamSize(); i++) {
                contestantList.add(new Contestant());
            }

            model.addObject("contestantList", contestantList);
            model.setViewName("race_registration");
            return model;
        }

    }

    @RequestMapping(value = "/race/{id}/addSoloContestant", method = RequestMethod.POST)
    public ModelAndView addSoloContestant(@RequestParam("category") String category, @PathVariable("id") int race_id) {

        Contestant contestant;
        contestant = userToContestant(userController.getUser());
        contestant.setPaid(false);
        contestant.setCategory(category);
        contestant.setRaceId(race_id);
        contestantService.saveContestant(contestant);

        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);

        if (race == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        } else {
            model.addObject("race", race);
            model.addObject("user", userController.getUser());
            model.addObject("result", "Registration completed successfully.");
            model.setViewName("race_registration");
            return model;
        }

    }

    private Contestant userToContestant(User user) {
        Contestant contestant = new Contestant();
        contestant.setEmail(user.getEmail());
        contestant.setFirstname(user.getFirstname());
        contestant.setLastname(user.getSurname());
        return contestant;
    }

    @RequestMapping(value = "/avaible_races", method = RequestMethod.GET)
    public ModelAndView avaibleRaces() {
        ModelAndView model = new ModelAndView();
        List<Race> list;
        list = raceService.getRacesToRegistration();
        model.setViewName("available_races");
        model.addObject("races", list);
        return model;
    }

    @RequestMapping(value = "/evaluated_races", method = RequestMethod.GET)
    public ModelAndView evaluatedRaces() {
        ModelAndView model = new ModelAndView();
        List<Race> list;
        list = raceService.getEvalutedRaces();
        model.setViewName("evaluated_races");
        model.addObject("races", list);
        return model;
    }

    @RequestMapping(value = "/race/{id}/addTeamByAdmin", method = RequestMethod.POST)
    public ModelAndView adminContestantsRegistration(@ModelAttribute("contestantList") ContestantList contestantList) {
        List<Contestant> contestants = contestantList.getContestants();
        ModelAndView model = new ModelAndView();
        model.setViewName("test");
        model.addObject("c", contestants);
        return model;
    }


}