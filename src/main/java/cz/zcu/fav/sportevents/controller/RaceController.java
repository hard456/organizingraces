package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.container.ContestantList;
import cz.zcu.fav.sportevents.form.CreateRaceForm;
import cz.zcu.fav.sportevents.form.SoloRegForm;
import cz.zcu.fav.sportevents.form.TeamRegForm;
import cz.zcu.fav.sportevents.model.*;
import cz.zcu.fav.sportevents.service.*;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
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

    @Autowired
    TeamSubcategoryService teamSubcategoryService;

    @Autowired
    ContestantSubcategoryService contestantSubcategoryService;

    @Autowired
    TeamService teamService;

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

    private boolean validCreateEventData(CreateRaceForm createRaceForm) {
        if (createRaceForm.getTeamRadio().equals("defaultValue")) {
            if (createRaceForm.getDefTeamCategoryId() == null) {
                return false;
            }
            if (!teamCategoryService.exist(createRaceForm.getDefTeamCategoryId())) {
                return false;
            }
        }
        if (createRaceForm.getConRadio().equals("defaultValue")) {
            if (createRaceForm.getDefConCategoryId() == null) {
                return false;
            }
            if (!contestantCategoryService.exist(createRaceForm.getDefConCategoryId())) {
                return false;
            }
        }
        if (createRaceForm.getTeamRadio().equals("own")) {
            if (createRaceForm.getTeamSubCategories().size() < 2 || createRaceForm.getTeamSubCategories().size() > 20) {
                return false;
            }
            for (TeamSubcategory list : createRaceForm.getTeamSubCategories()) {
                if (list.getName().isEmpty()) {
                    return false;
                }
            }
        }
        if (createRaceForm.getConRadio().equals("own")) {
            if (createRaceForm.getContestantSubCategories().size() < 2 || createRaceForm.getContestantSubCategories().size() > 20) {
                return false;
            }
            for (ContestantSubcategory list : createRaceForm.getContestantSubCategories()) {
                if (list.getName().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validCreateRaceParameters(HttpServletRequest request) {
        if (!request.getParameterMap().containsKey("race.teamSize")) {
            return false;
        }
        if (!request.getParameterMap().containsKey("race.name")) {
            return false;
        }
        if (!request.getParameterMap().containsKey("conRadio")) {
            return false;
        }
        if (!request.getParameterMap().containsKey("teamRadio")) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/create_event", method = RequestMethod.POST)
    public ModelAndView createEvent(HttpServletRequest request, @Valid @ModelAttribute("createRaceForm") CreateRaceForm createRaceForm, BindingResult result) {
        ModelAndView model = new ModelAndView();
        User user = userController.getUser();
        if (user != null) {
            model.setViewName("race_create_result");
            if (result.hasErrors()) {
                model.addObject("invalid", true);
                return model;
            }
            if (!validCreateRaceParameters(request)) {
                model.addObject("invalid", true);
                return model;
            }
            Race race = new Race();
            race.setName(HtmlUtils.htmlEscape(createRaceForm.getRace().getName(), "UTF-8"));
            race.setTeamSize(createRaceForm.getRace().getTeamSize());
            race.setUser(user);

            if (race.getName().length() > 32 || race.getName().length() < 3) {
                model.addObject("invalid", true);
                return model;
            }

            if (!raceService.isExistRaceByName(race.getName())) {
                if (validCreateEventData(createRaceForm)) {

                    if (createRaceForm.getTeamRadio().equals("own")) {
                        if (escapeTeamSubCategories(createRaceForm.getTeamSubCategories()) == null) {
                            model.addObject("invalid", true);
                            return model;
                        }
                    }
                    if (createRaceForm.getConRadio().equals("own")) {
                        if (escapeConSubCategories(createRaceForm.getContestantSubCategories()) == null) {
                            model.addObject("invalid", true);
                            return model;
                        }

                    }

                    List<ContestantSubcategory> conSubCategories = escapeConSubCategories(createRaceForm.getContestantSubCategories());
                    List<TeamSubcategory> teamSubCategories = escapeTeamSubCategories(createRaceForm.getTeamSubCategories());

                    if (createRaceForm.getConRadio().equals("none") && createRaceForm.getTeamRadio().equals("none")) {
                        raceService.save(race);
                    } else if (createRaceForm.getConRadio().equals("none") && createRaceForm.getTeamRadio().equals("defaultValue")) {
                        race.setTeamCategory(teamCategoryService.getCategoryById(createRaceForm.getDefTeamCategoryId()));
                        raceService.save(race);
                    } else if (createRaceForm.getConRadio().equals("none") && createRaceForm.getTeamRadio().equals("own")) {
                        TeamCategory category = new TeamCategory();
                        teamCategoryService.save(category);
                        teamSubCategories = setTeamCategoryIdToList(teamSubCategories, category.getId());
                        teamSubcategoryService.saveList(teamSubCategories);
                        race.setTeamCategory(category);
                        raceService.save(race);
                    } else if (createRaceForm.getConRadio().equals("defaultValue") && createRaceForm.getTeamRadio().equals("none")) {
                        race.setContestantCategory(contestantCategoryService.getCategoryById(createRaceForm.getDefConCategoryId()));
                        raceService.save(race);
                    } else if (createRaceForm.getConRadio().equals("defaultValue") && createRaceForm.getTeamRadio().equals("defaultValue")) {
                        race.setTeamCategory(teamCategoryService.getCategoryById(createRaceForm.getDefTeamCategoryId()));
                        race.setContestantCategory(contestantCategoryService.getCategoryById(createRaceForm.getDefConCategoryId()));
                        raceService.save(race);
                    } else if (createRaceForm.getConRadio().equals("defaultValue") && createRaceForm.getTeamRadio().equals("own")) {
                        TeamCategory category = new TeamCategory();
                        teamCategoryService.save(category);
                        teamSubCategories = setTeamCategoryIdToList(teamSubCategories, category.getId());
                        teamSubcategoryService.saveList(teamSubCategories);
                        race.setTeamCategory(category);
                        race.setContestantCategory(contestantCategoryService.getCategoryById(createRaceForm.getDefConCategoryId()));
                        raceService.save(race);
                    } else if (createRaceForm.getConRadio().equals("own") && createRaceForm.getTeamRadio().equals("none")) {
                        ContestantCategory conCategory = new ContestantCategory();
                        contestantCategoryService.save(conCategory);
                        conSubCategories = setConCategoryIdToList(conSubCategories, conCategory.getId());
                        contestantSubcategoryService.saveList(conSubCategories);
                        race.setContestantCategory(conCategory);
                        raceService.save(race);
                    } else if (createRaceForm.getConRadio().equals("own") && createRaceForm.getTeamRadio().equals("defaultValue")) {
                        ContestantCategory conCategory = new ContestantCategory();
                        contestantCategoryService.save(conCategory);
                        conSubCategories = setConCategoryIdToList(conSubCategories, conCategory.getId());
                        contestantSubcategoryService.saveList(conSubCategories);
                        race.setContestantCategory(conCategory);
                        race.setTeamCategory(teamCategoryService.getCategoryById(createRaceForm.getDefTeamCategoryId()));
                        raceService.save(race);
                    } else if (createRaceForm.getConRadio().equals("own") && createRaceForm.getTeamRadio().equals("own")) {
                        ContestantCategory conCategory = new ContestantCategory();
                        contestantCategoryService.save(conCategory);
                        conSubCategories = setConCategoryIdToList(conSubCategories, conCategory.getId());
                        contestantSubcategoryService.saveList(conSubCategories);
                        TeamCategory teamCategory = new TeamCategory();
                        teamCategoryService.save(teamCategory);
                        teamSubCategories = setTeamCategoryIdToList(teamSubCategories, teamCategory.getId());
                        teamSubcategoryService.saveList(teamSubCategories);
                        race.setContestantCategory(conCategory);
                        race.setTeamCategory(teamCategory);
                        raceService.save(race);
                    }
                    model.addObject("result", true);
                    return model;

                } else {
                    model.addObject("invalid", true);
                    return model;
                }

            } else {
                model.addObject("result", false);
                return model;
            }

        } else {
            model.setViewName("login");
            return model;
        }
    }

    private List<ContestantSubcategory> setConCategoryIdToList(List<ContestantSubcategory> conSubcategory, int id) {
        List<ContestantSubcategory> newList = new ArrayList<>();
        for (ContestantSubcategory list : conSubcategory) {
            list.setContestantCategory(contestantCategoryService.getCategoryById(id));
            newList.add(list);
        }
        return newList;
    }

    private List<TeamSubcategory> setTeamCategoryIdToList(List<TeamSubcategory> teamSubcategory, int id) {
        List<TeamSubcategory> newList = new ArrayList<>();
        for (TeamSubcategory list : teamSubcategory) {
            list.setTeamCategory(teamCategoryService.getCategoryById(id));
            newList.add(list);
        }
        return newList;
    }

    private List<TeamSubcategory> escapeTeamSubCategories(List<TeamSubcategory> teamSubcategory) {
        List<TeamSubcategory> newList = new ArrayList<>();
        for (TeamSubcategory list : teamSubcategory) {
            list.setName(HtmlUtils.htmlEscape(list.getName(), "UTF-8"));
            if (list.getName().length() > 20) {
                newList = null;
                break;
            } else {
                newList.add(list);
            }
        }
        return newList;
    }

    private List<ContestantSubcategory> escapeConSubCategories(List<ContestantSubcategory> conSubcategory) {
        List<ContestantSubcategory> newList = new ArrayList<>();
        for (ContestantSubcategory list : conSubcategory) {
            list.setName(HtmlUtils.htmlEscape(list.getName(), "UTF-8"));
            if (list.getName().length() > 20) {
                newList = null;
                break;
            } else {
                newList.add(list);
            }
        }
        return newList;
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
            if ((user = userController.getUser()) != null) {
                if (raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
                    model.addObject("race_cooperator", true);
                } else {
                    model.addObject("race_cooperator", false);
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
            if ((user = userController.getUser()) != null) {
                if (raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
                    model.addObject("race_cooperator", true);
                } else {
                    model.addObject("race_cooperator", false);
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
            if ((user = userController.getUser()) != null) {
                if (raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
                    model.addObject("race_cooperator", true);
                } else {
                    model.addObject("race_cooperator", false);
                }
            }

            if (race.getContestantCategory() != null) {
                List<ContestantSubcategory> contestantSubcategories;
                contestantSubcategories = contestantSubcategoryService.getListByCategoryId(race.getContestantCategory().getId());
                model.addObject("con_categories", contestantSubcategories);
            }
            if (race.getTeamCategory() != null) {
                List<TeamSubcategory> teamSubcategories;
                teamSubcategories = teamSubcategoryService.getListByCategoryId(race.getTeamCategory().getId());
                model.addObject("team_categories", teamSubcategories);
            }

            model.addObject("race", race);
            model.addObject("user", user);

            model.setViewName("race_registration");
            return model;
        }

    }

    @RequestMapping(value = "/race/{id}/addSoloContestant", method = RequestMethod.POST)
    public ModelAndView addSoloContestant(HttpServletRequest r, @Valid @ModelAttribute SoloRegForm soloRegForm,
                                          BindingResult bindingResult, @PathVariable("id") int race_id) {

        ModelAndView model = new ModelAndView();
        model.setViewName("race_reg_result");

        if (bindingResult.hasErrors()) {
            model.addObject("invalid", true);
            model.addObject("result", "Something went wrong.");
            return model;
        }

        Race race = raceService.getRaceById(race_id);
        User user = userController.getUser();

        if (user == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        }

        if (race == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        }

        if(contestantService.getListByUserAndRaceId(user.getId(),race.getId()) != null){
            model.addObject("invalid", true);
            model.addObject("result", "You can't register again to this race.");
            return model;
        }

        List<ContestantSubcategory> subcategories;
        subcategories = contestantSubcategoryService.getListByCategoryId(race.getContestantCategory().getId());
        ContestantSubcategory conCategory = null;
        if (subcategories != null) {
            if (!r.getParameterMap().containsKey("category")) {
                model.addObject("invalid", true);
                model.addObject("result", "Something went wrong.");
                return model;
            }
            conCategory = getConCategoryFromList(subcategories, soloRegForm.getCategory());
            if (conCategory == null) {
                model.addObject("invalid", true);
                model.addObject("result", "Something went wrong.");
                return model;
            }
        }

        Contestant contestant = new Contestant();
        contestant.setEmail(user.getEmail());
        contestant.setFirstname(user.getFirstname());
        contestant.setLastname(user.getSurname());
        contestant.setPaid(false);
        contestant.setCategory(conCategory);
        contestant.setRace(race);
        contestant.setPhone(user.getPhone());
        contestant.setUser(user);
        contestant.setTeam(null);
        contestantService.saveContestant(contestant);

        model.addObject("invalid", false);
        model.addObject("result", "Registration completed successfully.");
        return model;

    }

    private TeamSubcategory getTeamCategoryFromList(List<TeamSubcategory> list, int id) {
        for (TeamSubcategory item : list) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    private ContestantSubcategory getConCategoryFromList(List<ContestantSubcategory> list, int id) {
        for (ContestantSubcategory item : list) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
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

    @RequestMapping(value = "/race/{id}/teamRegistration", method = RequestMethod.POST)
    public ModelAndView teamRegistration(HttpServletRequest r, @ModelAttribute("teamRegForm") TeamRegForm teamRegForm,
                                         BindingResult bindingResult, @PathVariable("id") int race_id) {

        ModelAndView model = new ModelAndView();
        model.setViewName("race_reg_result");

        if (bindingResult.hasErrors()) {
            model.addObject("invalid", true);
            model.addObject("result", "Something went wrong.");
            return model;
        }

        Race race = raceService.getRaceById(race_id);
        User user = userController.getUser();
        Contestant creator = new Contestant();
        Team team = new Team();

        if (user == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        }

        if (race == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        }

        if(contestantService.getListByUserAndRaceId(user.getId(),race.getId()) != null){
            model.addObject("invalid", true);
            model.addObject("result", "You can't register again to same race.");
            return model;
        }

        if (race.getTeamCategory() != null) {
            if (!r.getParameterMap().containsKey("teamCategory")) {
                model.addObject("invalid", true);
                model.addObject("result", "Something went wrong.");
                return model;
            }
            List<TeamSubcategory> teamSubList;
            teamSubList = teamSubcategoryService.getListByCategoryId(race.getTeamCategory().getId());
            team.setCategory(getTeamCategoryFromList(teamSubList, teamRegForm.getTeamCategory()));
            if (team.getCategory() == null) {
                model.addObject("invalid", true);
                model.addObject("result", "Something went wrong.");
                return model;
            }
        }

        if (race.getContestantCategory() != null) {
            if (!r.getParameterMap().containsKey("conCategory")) {
                model.addObject("invalid", true);
                model.addObject("result", "Something went wrong.");
                return model;
            }
            List<ContestantSubcategory> conSubList;
            conSubList = contestantSubcategoryService.getListByCategoryId(race.getContestantCategory().getId());
            creator.setCategory(getConCategoryFromList(conSubList, teamRegForm.getConCategory()));
            if (creator.getCategory() == null) {
                model.addObject("invalid", true);
                model.addObject("result", "Something went wrong.");
                return model;
            }
        }

        team.setRace(race);
        creator.setFirstname(user.getFirstname());
        creator.setLastname(user.getSurname());
        creator.setPaid(false);
        creator.setEmail(user.getEmail());
        creator.setPhone(user.getPhone());
        creator.setUser(user);
        creator.setRace(race);

        if (race.getTeamSize() > 1) {
            List<Contestant> contestants;

            if (teamRegForm.getContestants().size() != (race.getTeamSize() - 1)) {
                model.addObject("invalid", true);
                model.addObject("result", "Something went wrong.");
                return model;
            }

            if (race.getContestantCategory() != null) {
                if (!validConListParameters(r, race.getTeamSize() - 1, true)) {
                    model.addObject("invalid", true);
                    model.addObject("result", "Something went wrong.");
                    return model;
                }
                contestants = validContestantList(teamRegForm.getContestants());
                if (contestants == null) {
                    model.addObject("invalid", true);
                    model.addObject("result", "Data are invalid:<br>Firstname (3 - 32 length)<br>Lastname (3 - 32 length)<br>Phone (123456789, 123 456 789, +420123456789, +420 123 456 789)");
                    return model;
                }

                contestants = setCategoriesToConList(contestants, teamRegForm.getTeammateCategory(), race.getContestantCategory().getId());
                if (contestants == null) {
                    model.addObject("invalid", true);
                    model.addObject("result", "Something went wrong.");
                    return model;
                }
            } else {
                if (!validConListParameters(r, race.getTeamSize() - 1, false)) {
                    model.addObject("invalid", true);
                    model.addObject("result", "Something went wrong.");
                    return model;
                }
                contestants = validContestantList(teamRegForm.getContestants());
                if (contestants == null) {
                    model.addObject("invalid", true);
                    model.addObject("result", "Something went wrong.");
                    return model;
                }

            }
            if (teamRegForm.getTeamName().length() != 0) {
                team.setName(HtmlUtils.htmlEscape(teamRegForm.getTeamName(), "UTF-8"));
                if (team.getName().length() < 3 || team.getName().length() > 32) {
                    model.addObject("invalid", true);
                    model.addObject("result", "Something went wrong.");
                    return model;
                }
            }
            teamService.save(team);
            for (Contestant c : contestants) {
                c.setUser(user);
                c.setRace(race);
                c.setTeam(team);
                contestantService.saveContestant(c);
            }
            creator.setTeam(team);
            contestantService.saveContestant(creator);
        } else {
            teamService.save(team);
            creator.setTeam(team);
            contestantService.saveContestant(creator);
        }

        model.addObject("request", r.getParameterMap());
        model.addObject("invalid", false);
        model.addObject("result", "Registration completed successfully.");
        return model;

    }

    private List<Contestant> validContestantList(List<Contestant> c) {

        for (int i = 0; i < c.size(); i++) {
            c.get(i).setFirstname(HtmlUtils.htmlEscape(c.get(i).getFirstname(), "UTF-8"));
            c.get(i).setLastname(HtmlUtils.htmlEscape(c.get(i).getLastname(), "UTF-8"));
            c.get(i).setEmail(HtmlUtils.htmlEscape(c.get(i).getEmail(), "UTF-8"));
            c.get(i).setPhone(HtmlUtils.htmlEscape(c.get(i).getPhone(), "UTF-8"));

            if (c.get(i).getEmail().length() > 0) {
                if (!EmailValidator.getInstance().isValid(c.get(i).getEmail()) || c.get(i).getEmail().length() > 32 || c.get(i).getEmail().length() < 6) {
                    return null;
                }
            }
            if (c.get(i).getPhone().length() > 0) {
                if (!c.get(i).getPhone().matches("^(\\+420)? ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}$")) {
                    return null;
                }
            }
            if (c.get(i).getFirstname().length() < 3 || c.get(i).getFirstname().length() > 32) {
                return null;
            }
            if (c.get(i).getLastname().length() < 3 || c.get(i).getLastname().length() > 32) {
                return null;
            }
        }
        return c;

    }

    private boolean validConListParameters(HttpServletRequest request, int listSize, boolean category) {
        for (int i = 0; i < listSize; i++) {
            if (!request.getParameterMap().containsKey("contestants[" + i + "].firstname")) {
                return false;
            }
            if (!request.getParameterMap().containsKey("contestants[" + i + "].lastname")) {
                return false;
            }
            if (!request.getParameterMap().containsKey("contestants[" + i + "].phone")) {
                return false;
            }
            if (!request.getParameterMap().containsKey("contestants[" + i + "].email")) {
                return false;
            }
            if (category) {
                if (!request.getParameterMap().containsKey("teammateCategory[" + i + "]")) {
                    return false;
                }
            }

        }
        return true;
    }

    private List<Contestant> setCategoriesToConList(List<Contestant> c, List<Integer> conSub, int category_id) {
        List<ContestantSubcategory> list;
        list = contestantSubcategoryService.getListByCategoryId(category_id);
        if (c.size() != conSub.size()) {
            return null;
        }
        for (int i = 0; i < c.size(); i++) {
            c.get(i).setCategory(contestantSubcategoryService.getSubCategoryById(conSub.get(i)));
            if (!isConSubCategoryInList(list, c.get(i).getCategory())) {
                return null;
            }
        }
        return c;
    }

    private boolean isConSubCategoryInList(List<ContestantSubcategory> l, ContestantSubcategory f) {
        for (ContestantSubcategory item : l) {
            if (item.getName().equals(f.getName())) {
                return true;
            }

        }
        return false;
    }

}