package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.comparator.FinalPointsCompare;
import cz.zcu.fav.sportevents.container.TeamIdListResultResponse;
import cz.zcu.fav.sportevents.form.DateTimeCategoryIdForm;
import cz.zcu.fav.sportevents.form.DatetimeTeamForm;
import cz.zcu.fav.sportevents.model.*;
import cz.zcu.fav.sportevents.service.*;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
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

            teams = editTeamName(teams, contestants);

            LinkedList<Team> teamLinkedList = new LinkedList<>();

            for (Team team : teams) {
                teamLinkedList.add(team);
            }

            teamLinkedList = countFinalPoints(teamLinkedList);
            teamLinkedList = countResultTime(teamLinkedList);
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

    private LinkedList<Team> countResultTime(LinkedList<Team> teams) {
        Team team;
        final PeriodFormatter periodFormat =
                new PeriodFormatterBuilder()
                        .appendYears()
                        .appendSuffix(" year", " years")
                        .appendSeparator(" ")
                        .appendMonths()
                        .appendSuffix(" month", " months")
                        .appendSeparator(" ")
                        .appendDays()
                        .appendSuffix(" day", " days")
                        .appendSeparator(" ")
                        .printZeroIfSupported()
                        .minimumPrintedDigits(2)
                        .appendHours()
                        .appendSeparator(":")
                        .appendMinutes()
                        .printZeroIfSupported()
                        .minimumPrintedDigits(2)
                        .appendSeparator(":")
                        .appendSeconds()
                        .minimumPrintedDigits(2)
                        .toFormatter();

        for (int i = 0; i < teams.size(); i++) {
            team = teams.get(i);
            if (team.getStartTime() != null && team.getFinishTime() != null) {
                Period diff = new Period(team.getStartTime(), team.getFinishTime()).normalizedStandard();
                team.setResultTime(periodFormat.print(diff));
            }
            teams.set(i, team);
        }
        return teams;
    }

    private int getPenalizationPoints(Team team) {
        int penalizationPoints = 0;
        Duration interval = null;
        try {

            interval = new Duration(team.getStartTime().plusMinutes(team.getDeadlineTime()), team.getFinishTime());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (interval != null) {
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

            List<Team> teams = teamService.getTeamsByRaceId(race_id);
            List<Contestant> contestants = contestantService.getContestantsByRaceId(race_id);
            model.addObject("race", race);
            model.addObject("user", userService.getLoginUser());
            model.setViewName("race/results_manage");

            teams = editTeamName(teams, contestants);

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

    private List<Team> editTeamName(List<Team> teams, List<Contestant> contestants) {
        String teamName;
        int counter;
        for (int i = 0; i < teams.size(); i++) {
            teamName = "";
            counter = 0;
            Team team = teams.get(i);

            if (team.getName() != null) {
                teamName = team.getName() + " (";
            }

            for (Contestant c : contestants) {
                if (c.getTeam() != null && c.getTeam().getId() == team.getId()) {

                    if (counter > 0) {
                        teamName += ", ";
                    }
                    String contestant = c.getFirstname() + " " + c.getLastname();
                    teamName += contestant;
                    counter++;
                }
            }

            if (team.getName() != null) {
                teamName += ")";
            }

            team.setName(teamName);

            teams.set(i, team);
        }
        return teams;
    }

    @RequestMapping(value = "/race/{id}/results/setStartTimeToCategory", method = RequestMethod.POST, produces = "application/json")
    public
    @ResponseBody
    TeamIdListResultResponse setStartTimeToCategory(@ModelAttribute DateTimeCategoryIdForm dateTimeCategoryIdForm,
                                                    BindingResult bindingResult, @PathVariable("id") int race_id) {

        TeamIdListResultResponse response = new TeamIdListResultResponse();
        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime newDate = null;

        if (bindingResult.hasErrors()) {
            response.setValidation("something_went_wrong");
            return response;
        }

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            response.setValidation("something_went_wrong");
            return response;
        }

        if (dateTimeCategoryIdForm.getCategoryId() == null) {
            response.setValidation("something_went_wrong");
            return response;
        }


        List<Team> teams = teamService.getListByCategoryIdRaceId(race_id, dateTimeCategoryIdForm.getCategoryId());

        if (teams.size() == 0) {
            response.setValidation("not_team");
            return response;
        }

        if (dateTimeCategoryIdForm.getDatetime() == null || dateTimeCategoryIdForm.getDatetime().equals("")) {
            response.setValidation("empty_datetime");
            return response;
        } else {

            try {
                newDate = format.parseDateTime(dateTimeCategoryIdForm.getDatetime());

            } catch (Exception e) {
                response.setValidation("wrong_format");
                return response;
            }

            for (Team team : teams) {
                if (team.getFinishTime() != null && !newDate.isBefore(team.getFinishTime())) {
                    response.setValidation("start_time_before");
                    return response;
                }
            }
            for (Team team : teams) {
                team.setStartTime(newDate);
                response.addToList(team.getId());
                teamService.update(team);
            }
        }

        response.setValidation("ok");
        return response;
    }

    @RequestMapping(value = "/race/{id}/results/setStartTimeNextTen", method = RequestMethod.POST, produces = "application/json")
    public
    @ResponseBody
    TeamIdListResultResponse setStartTimeNextTen(@ModelAttribute("dateTime") String dateTime, @PathVariable("id") int race_id) {
        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();
        TeamIdListResultResponse response = new TeamIdListResultResponse();

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            response.setValidation("something_went_wrong");
            return response;
        }

        List<Team> teams = teamService.getTeamsByRaceId(race_id);
        List<Team> newTeamList = new ArrayList<>();
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime newDate = null;

        if (dateTime == null || dateTime.equals("")) {
            response.setValidation("empty_datetime");
            return response;
        } else {

            try {
                newDate = format.parseDateTime(dateTime);

            } catch (Exception e) {
                response.setValidation("wrong_format");
                return response;
            }
            if (teams.size() == 0) {
                response.setValidation("not_team");
                return response;
            } else {
                for (Team team : teams) {
                    if (team.getStartTime() == null) {
                        if (newTeamList.size() == 10) {
                            break;
                        }
                        if (team.getFinishTime() != null && !newDate.isBefore(team.getFinishTime())) {
                            response.setValidation("start_time_before");
                            return response;
                        }
                        newTeamList.add(team);
                    }
                }
                if (newTeamList.size() == 0) {
                    response.setValidation("not_team");
                    return response;
                }
                for (Team team : newTeamList) {
                    team.setStartTime(newDate);
                    response.addToList(team.getId());
                    teamService.update(team);
                }
            }
        }

        response.setValidation("ok");
        return response;
    }


    @RequestMapping(value = "/race/{id}/results/setStartTimeAll", method = RequestMethod.POST, produces = "application/json")
    public
    @ResponseBody
    String setStartTimeAll(@ModelAttribute("dateTime") String dateTime, @PathVariable("id") int race_id) {

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "something_went_wrong";
        }

        List<Team> teams = teamService.getTeamsByRaceId(race_id);
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime newDate = null;

        if (dateTime.equals("")) {
            for (Team team : teams) {
                if (team.getFinishTime() != null) {
                    return "not_null_finishtime";
                }
            }
        } else {

            try {
                newDate = format.parseDateTime(dateTime);

            } catch (Exception e) {
                return "wrong_format";
            }

            for (Team team : teams) {
                if (team.getFinishTime() != null && !newDate.isBefore(team.getFinishTime())) {
                    return "start_time_before";
                }
            }
        }

        for (Team team : teams) {
            if (newDate != null) {
                team.setStartTime(newDate);
            } else {
                team.setStartTime(null);
            }
            teamService.update(team);
        }
        return "ok";
    }

    @RequestMapping(value = "/race/{id}/results/setStartTime", method = RequestMethod.POST)
    public
    @ResponseBody
    String setStartTime(@ModelAttribute("datetimeTeamForm") DatetimeTeamForm datetimeTeamForm, BindingResult bindingResult, @PathVariable("id") int race_id) {

        if (bindingResult.hasErrors()) {
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
            if (team.getFinishTime() == null) {
                team.setStartTime(null);
            } else {
                return "cant_be_empty";
            }
        } else {
            try {
                newDate = format.parseDateTime(datetimeTeamForm.getDateTime());
                team.setStartTime(newDate);
            } catch (Exception e) {
                return "wrong_format";
            }
            if (team.getFinishTime() != null && !newDate.isBefore(team.getFinishTime())) {
                return "start_time_before";
            }
        }

        teamService.update(team);

        return "ok";
    }

    @RequestMapping(value = "/race/{id}/results/finished", method = RequestMethod.POST, produces = "application/json")
    public
    @ResponseBody
    List<String> teamFinished(@ModelAttribute("teamId") Integer teamId, BindingResult bindingResult, @PathVariable("id") int race_id) {

        List<String> response = new ArrayList<>();

        if (bindingResult.hasErrors()) {
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

        if (team.getStartTime() == null) {
            response.add("start_time_missing");
            return response;
        }

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime = new DateTime();
        String responseDate = dateTime.toString(format);

        if (!team.getStartTime().isBefore(dateTime)) {
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
    public
    @ResponseBody
    String setFinishTime(@ModelAttribute("datetimeTeamForm") DatetimeTeamForm datetimeTeamForm, BindingResult bindingResult, @PathVariable("id") int race_id) {

        if (bindingResult.hasErrors()) {
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

        if (team.getStartTime() == null) {
            return "start_time_missing";
        }

        DateTimeFormatter format = DateTimeFormat.forPattern("yy-MM-dd HH:mm:ss");
        if (datetimeTeamForm.getDateTime().equals("")) {
            team.setFinishTime(null);
        } else {
            try {
                newDate = format.parseDateTime(datetimeTeamForm.getDateTime());

                if (!team.getStartTime().isBefore(newDate)) {
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
    public
    @ResponseBody
    String setPoints(@RequestBody List<String> numbers, @PathVariable("id") int race_id) {
        List<Integer> newNumbers = new ArrayList<>();
        if (numbers.size() != 3) {
            return "wrong_parameter_count";
        }

        for (String string : numbers) {
            try {
                newNumbers.add(Integer.parseInt(string));
            } catch (NumberFormatException e) {
                return "not_number";
            }

        }

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();
        Team team = teamService.getTeamById(newNumbers.get(0));

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "something_went_wrong";
        }

        if (team == null) {
            return "team";
        }

        if (newNumbers.get(1) < 0 || newNumbers.get(2) < 0) {
            return "negative_number";
        }

        team.setPoints(newNumbers.get(1));
        team.setBonus(newNumbers.get(2));
        teamService.update(team);

        return "ok";
    }

    @RequestMapping(value = "/race/{id}/results/setDeadlineToCategory", method = RequestMethod.POST)
    public
    @ResponseBody
    TeamIdListResultResponse setDeadlineToCategory(@ModelAttribute DateTimeCategoryIdForm dateTimeCategoryIdForm,
                                                   BindingResult bindingResult, @PathVariable("id") int race_id) {

        TeamIdListResultResponse response = new TeamIdListResultResponse();

        if (bindingResult.hasErrors()) {
            response.setValidation("number_format");
            return response;
        }

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();
        int minutes = 0;

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            response.setValidation("something_went_wrong");
            return response;
        }
        if (dateTimeCategoryIdForm.getDatetime() == null || dateTimeCategoryIdForm.getDatetime().equals("")) {
            response.setValidation("empty_time");
            return response;
        }

        try {
            minutes = Integer.parseInt(dateTimeCategoryIdForm.getDatetime());
        } catch (Exception e) {
            response.setValidation("number_format");
            return response;
        }

        if (minutes < 0) {
            response.setValidation("negative_number");
            return response;
        }

        List<Team> teams = teamService.getListByCategoryIdRaceId(race_id, dateTimeCategoryIdForm.getCategoryId());

        if (teams.size() == 0) {
            response.setValidation("not_team");
            return response;
        }

        for (Team team : teams) {
            team.setDeadlineTime(minutes);
            teamService.update(team);
            response.addToList(team.getId());
        }

        response.setValidation("ok");
        return response;
    }

    @RequestMapping(value = "/race/{id}/results/setDeadlineForAll", method = RequestMethod.POST)
    public
    @ResponseBody
    String setDeadlineForAll(@ModelAttribute("deadline") String deadline, @PathVariable("id") int race_id) {

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();
        int minutes = 0;

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "something_went_wrong";
        }
        if (deadline == null || deadline.equals("")) {
            return "empty_time";
        }

        try {
            minutes = Integer.parseInt(deadline);
        } catch (Exception e) {
            return "number_format";
        }

        if (minutes < 0) {
            return "negative_number";
        }

        List<Team> teams = teamService.getTeamsByRaceId(race_id);

        for (Team team : teams) {
            team.setDeadlineTime(minutes);
            teamService.update(team);
        }

        return "ok";
    }

}
