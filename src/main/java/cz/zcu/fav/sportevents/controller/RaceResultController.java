package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.model.Contestant;
import cz.zcu.fav.sportevents.model.Race;
import cz.zcu.fav.sportevents.model.Team;
import cz.zcu.fav.sportevents.model.User;
import cz.zcu.fav.sportevents.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class RaceResultController {

    @Autowired
    RaceService raceService;

    @Autowired
    UserService userService;

    @Autowired
    TeamService teamService;

    @Autowired
    RaceCooperationService raceCooperationService;

    @Autowired
    ContestantService contestantService;

    @RequestMapping(value = "/race/{id}/results", method = RequestMethod.GET)
    public ModelAndView results(@PathVariable("id") int race_id) {
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        if (race == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        } else {
            User user = userService.getLoginUser();
            List<Team> teams = teamService.getTeamsByRaceId(race_id);
            List<Contestant> contestants = contestantService.getContestantsByRaceId(race_id);
            model.addObject("race", race);
            model.addObject("user", userService.getLoginUser());
            model.setViewName("race/results");

            for (int i = 0; i<teams.size(); i++) {
                Team team = teams.get(i);
                if (team.getName() == null) {
                    String teamName = "";
                    for (Contestant c : contestants) {
                        if(c.getTeam() != null && c.getTeam().getId() == team.getId()){

                        if(teamName.length() > 0){
                            teamName += ", ";
                        }
                        String contestant = c.getFirstname() + " " + c.getLastname();
                        teamName += contestant;

                        }
                    }
                    team.setName(teamName);
                }
                teams.set(i,team);
            }

            model.addObject("teams", teams);

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

}
