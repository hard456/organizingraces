package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.form.DeleteTeamForm;
import cz.zcu.fav.sportevents.model.Race;
import cz.zcu.fav.sportevents.model.Team;
import cz.zcu.fav.sportevents.model.User;
import cz.zcu.fav.sportevents.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TeamController {

    @Autowired
    RaceService raceService;

    @Autowired
    UserService userService;

    @Autowired
    TeamService teamService;

    @Autowired
    ContestantService contestantService;

    @Autowired
    RaceCooperationService raceCooperationService;

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
            model.addObject("user", userService.getLoginUser());
            model.setViewName("race/teams");
            model.addObject("teams", teamService.getTeamsByRaceId(race_id));
            model.addObject("contestants", contestantService.getContestantsByRaceId(race_id));
            return model;
        }
    }

    @RequestMapping(value = "/race/{id}/teams/deleteTeam", method = RequestMethod.POST)
    public @ResponseBody
    Integer createTeam(HttpServletRequest r, @ModelAttribute DeleteTeamForm deleteTeamForm,
                                      BindingResult bindingResult, @PathVariable("id")int race_id) {

        User user = userService.getLoginUser();
        Race race = raceService.getRaceById(race_id);

        if (bindingResult.hasErrors()) {
            return -1;
        }

        Team team = teamService.getTeamById(deleteTeamForm.getTeamId());

        if (user == null || race == null) {
            return -1;
        }

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return -1;
        }

        if(team.getRace().getId() != race.getId()){
            return -1;
        }

        if(deleteTeamForm.getDeleteContestants()){
            contestantService.deleteContestantsByTeamId(deleteTeamForm.getTeamId());
        }
        else{
            contestantService.removeTeamByTeamId(deleteTeamForm.getTeamId());
        }

        teamService.delete(team);

        return deleteTeamForm.getTeamId();

    }

}
