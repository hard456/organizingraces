package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.comparator.FinalPointsCompare;
import cz.zcu.fav.sportevents.container.TeamIdListResultResponse;
import cz.zcu.fav.sportevents.form.DateTimeCategoryIdForm;
import cz.zcu.fav.sportevents.form.DateTimeTeamForm;
import cz.zcu.fav.sportevents.form.PointsForm;
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

    /**
     * Zobrazení šablony s výsledky.
     * @param race_id
     * @return
     */
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

            if (race.getTeamCategory() != null) {
                List<TeamSubcategory> categories;
                categories = teamSubcategoryService.getListByCategoryId(race.getTeamCategory().getId());
                model.addObject("team_categories", categories);
            }

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

    /**
     * Pro výpočet finálních bodů spojového seznamu týmů.
     * @param teamLinkedList spojový seznam týmů
     * @return spojový seznam týmů
     */
    private LinkedList<Team> countFinalPoints(LinkedList<Team> teamLinkedList) {
        for (Team team : teamLinkedList) {
            if (team.getStartTime() != null && team.getFinishTime() != null) {
                int penalization = 0;
                if (team.getDeadlineTime() != null && team.getDeadlineTime() != 0) {
                    penalization = countPenalizationPoints(team);
                    team.setPenalization(penalization);
                }
                int finalPoints = team.getPoints() + team.getBonus() - penalization;
                team.setFinalPoints(finalPoints);
            }
        }
        return teamLinkedList;
    }

    /**
     * Spočítá výsledný čas všem týmům spojového seznamu týmů.
     * @param teams spojový seznam týmů
     * @return spojový seznam týmů
     */
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

    /**
     * Vypočítá penalizační body týmů
     * @param team tým
     * @return počet penalizačních bodů
     */
    private int countPenalizationPoints(Team team) {
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

    /**
     * Zobrazení šablony pro zadávání výsledků závodů.
     * @param race_id
     * @return
     */
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

    /**
     * Přidá za týmové jméno do závody závodníky týmů. V případě, že není týmové jméno definovaná, tak bude týmové
     * jménovat obsahovat závodníky oddělené čárkou.
     * @param teams list týmů
     * @param contestants list závodníků
     * @return list týmů
     */
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
                if ((c.getTeam() != null) && (c.getTeam().getId() == team.getId())) {

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

    /**
     * Nastavení startovního času pro všechny týmy podle vybrané kategorie.
     * @param dateTimeCategoryIdForm kontejner s daty
     * @param bindingResult
     * @param race_id
     * @return "something_went_wrong" - obecná chyba, "not_team" - seznam týmů je prázdný,
     * "empty_datetime" - zadaná prázdná hodnota, "wrong_format" - špatný formát data,
     * "start_time_before" - startovní čas není před časem dokončení, "ok" - v pořádku
     */
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

    /**
     * Nastavení dalším 10 (maximálně - může být i méně) týmům startovní čas.
     * @param dateTime startovní čas
     * @param race_id
     * @return "something_went_wrong" - obecná chyba, "not_team" - seznam týmů je prázdný,
     * "empty_datetime" - zadaná prázdná hodnota, "wrong_format" - špatný formát data,
     * "start_time_before" - startovní čas není před časem dokončení, "ok" - v pořádku
     */
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

    /**
     * Nastavení startovního času všem týmům.
     * @param dateTime startovní čas
     * @param race_id
     * @return "something_went_wrong" - obecná chyba, "not_team" - seznam týmů je prázdný,
     * "not_null_finishtime" - nelze nastavit prázdná hodnota (někde je vyplněn čas dokončení),
     * "wrong_format" - špatný formát data, "start_time_before" - startovní čas není před časem dokončení, "ok" - v pořádku
     */
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

    /**
     * Nastavení startovního času konkrétnímu týmu.
     * @param dateTimeTeamForm kontejner s daty z formuláře
     * @param bindingResult
     * @param race_id
     * @return "something_went_wrong" - obecná chyba, "team" - tým neexistuje, "collision" - kolize dat,
     * "cant_be_empty" - finální čas je vyplněn (nemůže být prázdný), "wrong_format" - špatný formát data,
     * "start_time_before" - startovní čas není před časem dokončení, "ok" - v pořádku
     */
    @RequestMapping(value = "/race/{id}/results/setStartTime", method = RequestMethod.POST)
    public
    @ResponseBody
    String setStartTime(@ModelAttribute("datetimeTeamForm") DateTimeTeamForm dateTimeTeamForm, BindingResult bindingResult, @PathVariable("id") int race_id) {

        if (bindingResult.hasErrors()) {
            return "something_went_wrong";
        }

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();
        DateTime newDate, dateTime;

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "something_went_wrong";
        }

        Team team = teamService.getTeamById(dateTimeTeamForm.getTeamId());

        if (team == null) {
            return "team";
        }

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        try{
            dateTime = format.parseDateTime(dateTimeTeamForm.getDateTime());
        }
        catch (Exception e){
            dateTime = null;
        }

        if(!Objects.equals(dateTime,team.getStartTime())){
            return "collision";
        }

        if (dateTimeTeamForm.getNewDateTime().equals("")) {
            if (team.getFinishTime() == null) {
                team.setStartTime(null);
            } else {
                return "cant_be_empty";
            }
        } else {
            try {
                newDate = format.parseDateTime(dateTimeTeamForm.getNewDateTime());
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

    /**
     * Přiřadí týmu jako čas dokončené aktuální čas a ten vrátí zpět pro potřeby nastavení do tabulky DataTables.
     * @param dateTimeTeamForm kontejner s daty
     * @param bindingResult
     * @param race_id
     * @return list řetězců - na pozici nula stav, na pozici dva když to bude v pořádku čas dokončení
     */
    @RequestMapping(value = "/race/{id}/results/finished", method = RequestMethod.POST, produces = "application/json")
    public
    @ResponseBody
    List<String> teamFinished(@ModelAttribute("dateTimeTeamForm") DateTimeTeamForm dateTimeTeamForm, BindingResult bindingResult, @PathVariable("id") int race_id) {

        List<String> response = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            response.add("something_went_wrong");
            return response;
        }
        DateTime newDateTime, dateTime;
        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();
        Team team = teamService.getTeamById(dateTimeTeamForm.getTeamId());

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            response.add("something_went_wrong");
            return response;
        }

        if(team == null){
            response.add("team");
            return response;
        }

        if (team.getStartTime() == null) {
            response.add("start_time_missing");
            return response;
        }

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        try{
            dateTime = format.parseDateTime(dateTimeTeamForm.getDateTime());
        }
        catch (Exception e){
            dateTime = null;
        }

        if(!Objects.equals(dateTime,team.getFinishTime())){
            response.add("collision");
            return response;
        }

        newDateTime = new DateTime();
        String responseDate = newDateTime.toString(format);

        if (!team.getStartTime().isBefore(newDateTime)) {
            response.add("start_time_before");
            return response;
        }

        team.setFinishTime(newDateTime);
        teamService.update(team);

        response.add("ok");
        response.add(responseDate);

        return response;
    }

    /**
     * Nastavení času dokončení ručně.
     * @param dateTimeTeamForm kontejner s daty z formuláře
     * @param bindingResult
     * @param race_id
     * @return "something_went_wrong" - obecná chyba, "team" - tým neexistuje, "collision" - kolize dat,
     * "cant_be_empty" - finální čas je vyplněn (nemůže být prázdný), "wrong_format" - špatný formát data,
     * "start_time_before" - startovní čas není před časem dokončení, "ok" - v pořádku,
     * "start_time_missing" - nelze zadat finální čas když není zadán čas startu
     */
    @RequestMapping(value = "/race/{id}/results/setFinishTime", method = RequestMethod.POST)
    public
    @ResponseBody
    String setFinishTime(@ModelAttribute("datetimeTeamForm") DateTimeTeamForm dateTimeTeamForm, BindingResult bindingResult, @PathVariable("id") int race_id) {

        if (bindingResult.hasErrors()) {
            return "something_went_wrong";
        }


        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();
        DateTime newDate, dateTime;
        Team team = teamService.getTeamById(dateTimeTeamForm.getTeamId());

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "something_went_wrong";
        }

        if (team == null) {
            return "team";
        }

        if(dateTimeTeamForm.getNewDateTime() == null){
            return "something_went_wrong";
        }

        if (team.getStartTime() == null) {
            return "start_time_missing";
        }

        DateTimeFormatter format = DateTimeFormat.forPattern("yy-MM-dd HH:mm:ss");

        try {
            dateTime = format.parseDateTime(dateTimeTeamForm.getDateTime());
        }
        catch (Exception e){
            dateTime = null;
        }

        if(!Objects.equals(dateTime,team.getFinishTime())){
            return "collision";
        }

        if (dateTimeTeamForm.getNewDateTime().equals("")) {
            team.setFinishTime(null);
        } else {
            try {
                newDate = format.parseDateTime(dateTimeTeamForm.getNewDateTime());
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

    /**
     * Nastavení bodů a bonusových bodů týmu.
     * @param pointsForm kontejner s daty z formuláře
     * @param bindingResult
     * @param race_id
     * @return "something_went_wrong" - obecná chyba, "not_number" - není číslo, "collision" - kolize dat,
     * "team" - tým neexistuje, "ok" - v pořádku
     */
    @RequestMapping(value = "/race/{id}/results/setPoints", method = RequestMethod.POST)
    public
    @ResponseBody
    String setPoints(@ModelAttribute("updatePointsForm") PointsForm pointsForm, BindingResult bindingResult, @PathVariable("id") int race_id) {

        if(bindingResult.hasErrors()){
            return "something_went_wrong";
        }

        if (hasUpdatePointsFormNullVar(pointsForm)) {
            return "not_number";
        }

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();
        Team team = teamService.getTeamById(pointsForm.getTeamId());

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "something_went_wrong";
        }

        if (team == null) {
            return "team";
        }

        if(team.getRace().getId() != race_id){
            return "something_went_wrong";
        }

        if(team.getPoints() != pointsForm.getPoints()){
            return "collision";
        }

        if(team.getBonus() != pointsForm.getBonus()){
            return "collision";
        }

        team.setPoints(pointsForm.getNewPoints());
        team.setBonus(pointsForm.getNewBonus());
        teamService.update(team);

        return "ok";
    }

    /**
     * Kontrola hodnota kontejneru, jestli je nějaké null.
     * @param pointsForm kontejner s daty z formuláře
     * @return true - nějaké je null, false - žádná není null
     */
    private boolean hasUpdatePointsFormNullVar(PointsForm pointsForm){
        if(pointsForm.getTeamId() == null){
            return true;
        }
        if(pointsForm.getNewPoints() == null){
            return true;
        }
        if(pointsForm.getNewBonus() == null){
            return true;
        }
        if(pointsForm.getPoints() == null){
            return true;
        }
        if(pointsForm.getBonus() == null){
            return true;
        }
        return false;
    }

    /**
     * Nastavení času pro dokončení závodu v minutách podle týmové podkategorie.
     * @param dateTimeCategoryIdForm kontejner s daty z formuláře
     * @param bindingResult
     * @param race_id
     * @return "number_format" - není číslo, "something_went_wrong" - obecná chyba,
     * "empty_time" - zadaná prázdná hodnota, "negative_number" - záparné číslo,
     * "not_team" - prázdný list týmů, "ok" - v pořádku
     */
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

    /**
     * Nastavení času pro dokončení závodu v minutách pro všechny týmy.
     * @param deadline čas v minutách pro dokončení závodu
     * @param race_id
     * @return "something_went_wrong" - obecná chyba, "empty_time" - prázdná hodnota, "number_format" - není číslo,
     * "negative_format" - je záporné číslo, "ok" - v pořádku
     */
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

    /**
     * Data pro refreshování tabulky DataTables.
     * @param race_id
     * @return list týmů
     */
    @RequestMapping(value = "/race/{id}/results/refreshTable", method = RequestMethod.POST)
    public
    @ResponseBody
    List<Team> refreshTable(@PathVariable("id") int race_id) {
        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();

        if (race == null || user == null) {
            return new ArrayList<>();
        }
        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return new ArrayList<>();
        }

        List<Team> teams = teamService.getTeamsByRaceId(race_id);
        List<Contestant> contestants = contestantService.getContestantsByRaceId(race_id);
        teams = editTeamName(teams, contestants);
        return teams;
    }

}
