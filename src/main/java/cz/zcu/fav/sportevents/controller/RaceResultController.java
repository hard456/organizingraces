package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.comparator.FinalPointsCompare;
import cz.zcu.fav.sportevents.container.ListIntegerStringCointaner;
import cz.zcu.fav.sportevents.form.DatetimeTeamForm;
import cz.zcu.fav.sportevents.model.*;
import cz.zcu.fav.sportevents.service.*;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

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
            if (team.getStartTime() != null && team.getFinishTime() != null) {
                int penalization = 0;
                if (team.getDeadlineTime() != null) {
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
    public @ResponseBody ListIntegerStringCointaner setStartTimeAll(@ModelAttribute("dateTime") String dateTime, @PathVariable("id") int race_id) {

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();
        ListIntegerStringCointaner response = new ListIntegerStringCointaner();

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            response.setString("something_went_wrong");
            return response;
        }

        List<Team> teams = teamService.getTeamsByRaceId(race_id);
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime newDate = null;

        if(dateTime.equals("")){
            for (Team team : teams){
                if(team.getFinishTime() != null){
                    response.setString("not_null_finishtime");
                    return response;
                }
            }
        }
        else{

            try {
                newDate = format.parseDateTime(dateTime);

            } catch (Exception e) {
                response.setString("wrong_format");
                return response;
            }

            for (Team team : teams){
                if(team.getFinishTime() != null && !format.parseDateTime(dateTime).isBefore(team.getFinishTime())){
                    response.setString("start_time_before");
                    return response;
                }
            }
        }

        for (Team team : teams) {
            if(newDate != null){
                team.setStartTime(newDate);
            }
            else{
                team.setStartTime(null);
            }

            response.addToList(team.getId());
//            teamService.update(team);
        }

        response.setString("ok");
        return response;
    }

    @RequestMapping(value = "/race/{id}/results/setStartTime", method = RequestMethod.POST)
    public
    @ResponseBody
    String setStartTime(@ModelAttribute("datetimeTeamForm") DatetimeTeamForm datetimeTeamForm, BindingResult bindingResult, @PathVariable("id") int race_id) {

        if(bindingResult.hasErrors()){
            return "something_went_wrong";
        }

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();
        DateTime newDate;

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "something_went_wrong";
        }

        if (datetimeTeamForm.getTeamId() == null) {
            return "team";
        }

        Team team = teamService.getTeamById(datetimeTeamForm.getTeamId());

        DateTimeFormatter format = DateTimeFormat.forPattern("yy-MM-dd HH:mm:ss");
        if (datetimeTeamForm.getDateTime().equals("")) {
            if(team.getFinishTime() == null){
                team.setStartTime(null);
            }
            else{
                return "cant_be_empty";
            }
        } else {
            try {
                newDate = format.parseDateTime(datetimeTeamForm.getDateTime());
                team.setStartTime(newDate);
            } catch (Exception e) {
                return "wrong_format";
            }
            if(team.getFinishTime() != null && !newDate.isBefore(team.getFinishTime())){
                return "start_time_before";
            }
        }

        teamService.update(team);

        return "ok";
    }

    //OK
    @RequestMapping(value = "/race/{id}/results/finished", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody List<String> teamFinished(@ModelAttribute("teamId") Integer teamId, BindingResult bindingResult, @PathVariable("id") int race_id) {

        List<String> response = new ArrayList<>();

        if(bindingResult.hasErrors()){
            response.add("something_went_wrong");
            return response;
        }

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();
        Team team = teamService.getTeamById(teamId);

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            response.add("something_went_wrong");
            return response;
        }

        if(team.getStartTime() == null){
            response.add("start_time_missing");
            return response;
        }

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime = new DateTime();
        String responseDate = dateTime.toString(format);

        if(!team.getStartTime().isBefore(dateTime)){
            response.add("start_time_before");
            return response;
        }

        team.setFinishTime(dateTime);
        teamService.update(team);

        response.add("ok");
        response.add(responseDate);

       return response;
    }

    @RequestMapping(value = "/race/{id}/results/setFinishTime", method = RequestMethod.POST)
    public @ResponseBody String setFinishTime(@ModelAttribute("datetimeTeamForm") DatetimeTeamForm datetimeTeamForm, BindingResult bindingResult, @PathVariable("id") int race_id) {

        if(bindingResult.hasErrors()){
            return "something_went_wrong";
        }

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();
        DateTime newDate;

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "something_went_wrong";
        }

        if (datetimeTeamForm.getTeamId() == null) {
            return "team";
        }

        Team team = teamService.getTeamById(datetimeTeamForm.getTeamId());

        if(team.getStartTime() == null){
            return "start_time_missing";
        }

        DateTimeFormatter format = DateTimeFormat.forPattern("yy-MM-dd HH:mm:ss");
        if (datetimeTeamForm.getDateTime().equals("")) {
            team.setFinishTime(null);
        } else {
            try {
                newDate = format.parseDateTime(datetimeTeamForm.getDateTime());

                if(!team.getStartTime().isBefore(newDate)){
                    return "start_time_before";
                }

                team.setFinishTime(newDate);
            } catch (Exception e) {
                return "wrong_format";
            }
        }

        teamService.update(team);

        return "ok";
    }

    @RequestMapping(value = "/race/{id}/results/setPoints", method = RequestMethod.POST)
    public @ResponseBody String setPoints(@RequestBody List<String> numbers, @PathVariable("id") int race_id) {
        List<Integer> newNumbers = new ArrayList<>();
        if(numbers.size() != 3){
            return "wrong_parameter_count";
        }

        for (String string : numbers){
                try {
                    newNumbers.add(Integer.parseInt(string));
                }
                catch (NumberFormatException e){
                    return "not_number";
                }

        }

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();
        Team team = teamService.getTeamById(newNumbers.get(0));

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "something_went_wrong";
        }

        if(team == null){
            return "team";
        }

        if(newNumbers.get(1) < 0 || newNumbers.get(2) < 0){
            return "negative_number";
        }

        team.setPoints(newNumbers.get(1));
        team.setBonus(newNumbers.get(2));
        teamService.update(team);

        return "ok";
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
