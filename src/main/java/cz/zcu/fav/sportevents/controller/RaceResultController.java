package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.comparator.FinalPointsCompare;
import cz.zcu.fav.sportevents.form.DatetimeTeamForm;
import cz.zcu.fav.sportevents.model.*;
import cz.zcu.fav.sportevents.service.*;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
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

    @Autowired
    TeamSubcategoryService teamSubcategoryService;

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

            teams = setNameToTeamsWithoutName(teams, contestants);

            LinkedList<Team> teamLinkedList = new LinkedList<>();

            for (Team team : teams) {
                teamLinkedList.add(team);
            }

            teamLinkedList = countFinalPoints(teamLinkedList);
            Collections.sort(teamLinkedList, new FinalPointsCompare());

            model.addObject("teams", teamLinkedList);

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

    private LinkedList<Team> countFinalPoints(LinkedList<Team> teamLinkedList) {
        for (Team team : teamLinkedList) {
            if(team.getStartTime() != null && team.getFinishTime() != null){
                int penalization = 0;
                if(team.getDeadlineTime() != null){
                    penalization = getPenalizationPoints(team);
                    team.setPenalization(penalization);
                }
                int finalPoints = team.getPoints() + team.getBonus() - penalization;
                team.setFinalPoints(finalPoints);
            }
        }
        return teamLinkedList;
    }

    private int getPenalizationPoints(Team team) {
        int penalizationPoints = 0;
        Duration interval = null;
        try {
            Date date = new Date();
            interval = new Duration(team.getStartTime().plusMinutes(team.getDeadlineTime()), team.getFinishTime());
//            interval = new Period(         deadlineTime, finishTime, PeriodType.minutes());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (interval != null) {
//            final int minutes = interval.getMinutes();
            long minutes = interval.getStandardMinutes();
            final Duration standardMinutes = Duration.standardMinutes(interval.getStandardMinutes());
            final Duration secondsInterval = new Duration(interval).minus(standardMinutes);
            final long seconds = secondsInterval.getStandardSeconds();
            if (minutes >= 0 && seconds > 0) {
                minutes += 1;
            }
            penalizationPoints = (int) (minutes);
        }
        if (penalizationPoints < 0) {
            penalizationPoints = 0;
        }
        return penalizationPoints;
    }

    @RequestMapping(value = "/race/{id}/results/manage", method = RequestMethod.GET)
    public ModelAndView resultsManage(@PathVariable("id") int race_id) {
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        if (race == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        } else {
            User user = userService.getLoginUser();

            if (race.getTeamCategory() != null) {
                List<TeamSubcategory> teamSubcategories;
                teamSubcategories = teamSubcategoryService.getListByCategoryId(race.getTeamCategory().getId());
                model.addObject("team_categories", teamSubcategories);
            }

//            Team yolo = teamService.getTeamById(153);
            DateTimeFormatter sdf = DateTimeFormat.forPattern("yy-MM-dd HH:mm:ss");
//            DateTime yoloDate = sdf.parseDateTime("2010-02-06 16:30:13");
//            yolo.setFinishTime(yoloDate);
//            yolo.setName("yolo");
//            teamService.save(yolo);

//            try {
//                sdf.parseDateTime("yolo");
//                System.out.println("OK");
//            }
//            catch (Exception e){
//                System.out.println("FAIL");
//            }

            List<Team> teams = teamService.getTeamsByRaceId(race_id);
            List<Contestant> contestants = contestantService.getContestantsByRaceId(race_id);
            model.addObject("race", race);
            model.addObject("user", userService.getLoginUser());
            model.setViewName("race/results_manage");

            teams = setNameToTeamsWithoutName(teams, contestants);

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

    private List<Team> setNameToTeamsWithoutName(List<Team> teams, List<Contestant> contestants) {
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            if (team.getName() == null) {
                String teamName = "";
                for (Contestant c : contestants) {
                    if (c.getTeam() != null && c.getTeam().getId() == team.getId()) {

                        if (teamName.length() > 0) {
                            teamName += ", ";
                        }
                        String contestant = c.getFirstname() + " " + c.getLastname();
                        teamName += contestant;

                    }
                }
                team.setName(teamName);
            }
            teams.set(i, team);
        }
        return teams;
    }

//OK
    @RequestMapping(value = "/race/{id}/results/setStartTimeNextTen", method = RequestMethod.POST)
    public ModelAndView setStartTimeNextTen(@ModelAttribute("dateTime") String dateTime, @PathVariable("id") int race_id) {
        return null;
    }

//OK
    @RequestMapping(value = "/race/{id}/results/setStartTimeAll", method = RequestMethod.POST)
    public ModelAndView setStartTimeAll(@ModelAttribute("dateTime") String dateTime, @PathVariable("id") int race_id) {
        return null;
    }

//OK
    @RequestMapping(value = "/race/{id}/results/setStartTime", method = RequestMethod.POST)
    public ModelAndView setStartTime(@ModelAttribute("datetimeTeamForm") DatetimeTeamForm datetimeTeamForm, @PathVariable("id") int race_id) {
        System.out.println("ayy");
        return null;
    }

//OK
    @RequestMapping(value = "/race/{id}/results/finished", method = RequestMethod.POST)
    public ModelAndView teamFinished(@ModelAttribute("teamId") Integer teamId, @PathVariable("id") int race_id) {

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();


        return null;
    }

//OK
    @RequestMapping(value = "/race/{id}/results/setFinishTime", method = RequestMethod.POST)
    public ModelAndView setFinishTime(@ModelAttribute("datetimeTeamForm") DatetimeTeamForm datetimeTeamForm, @PathVariable("id") int race_id) {
        System.out.println();
        return null;
    }

//OK dodělat přenos bodů
    @RequestMapping(value = "/race/{id}/results/setPoints", method = RequestMethod.POST)
    public ModelAndView setPoints(@PathVariable("id") int race_id) {

        return null;
    }

//TODO hodnoty
    @RequestMapping(value = "/race/{id}/results/setDeadlineToCategory", method = RequestMethod.POST)
    public ModelAndView setDeadlineToCategory(@ModelAttribute("dateTime") String dateTime, @PathVariable("id") int race_id) {
        return null;
    }

//TODO hodnoty
    @RequestMapping(value = "/race/{id}/results/setDeadlineForAll", method = RequestMethod.POST)
    public ModelAndView setDeadlineForAll(@ModelAttribute("dateTime") String dateTime, @PathVariable("id") int race_id) {
        return null;
    }

}
