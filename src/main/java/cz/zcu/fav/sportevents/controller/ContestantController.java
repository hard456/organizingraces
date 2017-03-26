package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.model.Contestant;
import cz.zcu.fav.sportevents.model.Race;
import cz.zcu.fav.sportevents.model.User;
import cz.zcu.fav.sportevents.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ContestantController {

    @Autowired
    RaceService raceService;

    @Autowired
    UserService userService;

    @Autowired
    RaceCooperationService raceCooperationService;

    @Autowired
    ContestantService contestantService;

    @Autowired
    ContestantSubcategoryService contestantSubcategoryService;

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
            if ((user = userService.getLoginUser()) != null) {
                if (raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
                    model.addObject("race_cooperator", true);
                } else {
                    model.addObject("race_cooperator", false);
                }
            }

            model.addObject("contestants", contestantService.getContestantsByRaceId(race_id));
            model.addObject("race", race);
            model.addObject("user", user);
            if (race.getContestantCategory() != null) {
                model.addObject("categories", contestantSubcategoryService.getListByCategoryId(race.getContestantCategory().getId()));
            }

            model.setViewName("race/contestants_list");
            return model;
        }
    }


    @RequestMapping(value = "/race/{id}/contestants/changePaidValue", method = RequestMethod.POST)
    public
    @ResponseBody
    int changePaidStatus(HttpServletRequest r, @ModelAttribute("contestantId") Integer contestantId,
                         BindingResult bindingResult, @PathVariable("id") int race_id) {

        User user = userService.getLoginUser();
        Race race = raceService.getRaceById(race_id);


        if (bindingResult.hasErrors()) {
            return -1;
        }

        if (user == null || race == null) {
            return -1;
        }

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return -1;
        }

        Contestant contestant = contestantService.getContestantById(contestantId);

        if(contestant == null){
            return -1;
        }

        if(contestant.isPaid()){
            contestant.setPaid(false);
            contestantService.update(contestant);
            return 0;
        }
        else{
            contestant.setPaid(true);
            contestantService.update(contestant);
            return 1;
        }

    }
}
