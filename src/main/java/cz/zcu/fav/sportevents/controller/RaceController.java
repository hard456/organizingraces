package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.container.ContestantList;
import cz.zcu.fav.sportevents.form.CreateRaceForm;
import cz.zcu.fav.sportevents.model.*;
import cz.zcu.fav.sportevents.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

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
            if (!contestantCategoryService.exist(createRaceForm.getDefTeamCategoryId())) {
                return false;
            }
        }
        if (createRaceForm.getConRadio().equals("defaultValue")) {
            if (!contestantCategoryService.exist(createRaceForm.getDefConCategoryId())) {
                return false;
            }
        }
        if (createRaceForm.getTeamRadio().equals("own")) {
            for (TeamSubcategory list : createRaceForm.getTeamSubCategories()) {
                if (list.getName().isEmpty()) {
                    return false;
                }
            }
        }
        if (createRaceForm.getConRadio().equals("own")) {
            for (ContestantSubcategory list : createRaceForm.getContestantSubCategories()) {
                if (list.getName().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    @RequestMapping(value = "/create_event", method = RequestMethod.POST)
    public ModelAndView createEvent(@Valid @ModelAttribute("createRaceForm") CreateRaceForm createRaceForm, BindingResult result) {
        ModelAndView model = new ModelAndView();
        User user = userController.getUser();
        if (user != null) {
            model.setViewName("race_create_result");

            if (result.hasErrors() || createRaceForm.getConRadio() == null || createRaceForm.getTeamRadio() == null) {
                model.addObject("invalid", true);
                return model;
            } else {
                Race race = new Race();
                race.setName(HtmlUtils.htmlEscape(createRaceForm.getRace().getName(), "UTF-8"));
                race.setTeamSize(createRaceForm.getRace().getTeamSize());
                race.setUser(user);
                if (!raceService.isExistRaceByName(race.getName())) {
                    if (validCreateEventData(createRaceForm)) {

                        List<ContestantSubcategory> conSubCategories = escapeConSubCategories(createRaceForm.getContestantSubCategories());
                        List<TeamSubcategory> teamSubCategories = escapeTeamSubCategories(createRaceForm.getTeamSubCategories());

                        if (createRaceForm.getConRadio().equals("none") && createRaceForm.getTeamRadio().equals("none")) {
                            raceService.save(race);
                        } else if (createRaceForm.getConRadio().equals("none") && createRaceForm.getTeamRadio().equals("defaultValue")) {
                            race.setTeamCategory(teamCategoryService.getCategoryById(createRaceForm.getDefTeamCategoryId()));
                            raceService.save(race);
                        } else if (createRaceForm.getConRadio().equals("none") && createRaceForm.getTeamRadio().equals("own")) {
                            if (teamSubCategories.size() >= 2) {
                                TeamCategory category = new TeamCategory();
                                teamCategoryService.save(category);
                                teamSubCategories = setTeamCategoryIdToList(teamSubCategories, category.getId());
                                teamSubcategoryService.saveList(teamSubCategories);
                                race.setTeamCategory(category);
                                raceService.save(race);
                            } else {
                                model.addObject("invalid", true);
                                return model;
                            }
                        } else if (createRaceForm.getConRadio().equals("defaultValue") && createRaceForm.getTeamRadio().equals("none")) {
                            race.setContestantCategory(contestantCategoryService.getCategoryById(createRaceForm.getDefConCategoryId()));
                            raceService.save(race);
                        } else if (createRaceForm.getConRadio().equals("defaultValue") && createRaceForm.getTeamRadio().equals("defaultValue")) {
                            race.setTeamCategory(teamCategoryService.getCategoryById(createRaceForm.getDefTeamCategoryId()));
                            race.setContestantCategory(contestantCategoryService.getCategoryById(createRaceForm.getDefConCategoryId()));
                            raceService.save(race);
                        } else if (createRaceForm.getConRadio().equals("defaultValue") && createRaceForm.getTeamRadio().equals("own")) {
                            if (teamSubCategories.size() >= 2) {
                                TeamCategory category = new TeamCategory();
                                teamCategoryService.save(category);
                                teamSubCategories = setTeamCategoryIdToList(teamSubCategories, category.getId());
                                teamSubcategoryService.saveList(teamSubCategories);
                                race.setTeamCategory(category);
                                race.setContestantCategory(contestantCategoryService.getCategoryById(createRaceForm.getDefConCategoryId()));
                                raceService.save(race);
                            } else {
                                model.addObject("invalid", true);
                                return model;
                            }
                        } else if (createRaceForm.getConRadio().equals("own") && createRaceForm.getTeamRadio().equals("none")) {
                            if (conSubCategories.size() >= 2) {
                                ContestantCategory conCategory = new ContestantCategory();
                                contestantCategoryService.save(conCategory);
                                conSubCategories = setConCategoryIdToList(conSubCategories, conCategory.getId());
                                contestantSubcategoryService.saveList(conSubCategories);
                                race.setContestantCategory(conCategory);
                                raceService.save(race);
                            } else {
                                model.addObject("invalid", true);
                                return model;
                            }
                        } else if (createRaceForm.getConRadio().equals("own") && createRaceForm.getTeamRadio().equals("defaultValue")) {
                            if (conSubCategories.size() >= 2) {
                                ContestantCategory conCategory = new ContestantCategory();
                                contestantCategoryService.save(conCategory);
                                conSubCategories = setConCategoryIdToList(conSubCategories, conCategory.getId());
                                contestantSubcategoryService.saveList(conSubCategories);
                                race.setContestantCategory(conCategory);
                                race.setTeamCategory(teamCategoryService.getCategoryById(createRaceForm.getDefTeamCategoryId()));
                                raceService.save(race);
                            } else {
                                model.addObject("invalid", true);
                                return model;
                            }
                        } else if (createRaceForm.getConRadio().equals("own") && createRaceForm.getTeamRadio().equals("own")) {
                            if (teamSubCategories.size() >= 2 && conSubCategories.size() >= 2) {
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
                            } else {
                                model.addObject("invalid", true);
                                return model;
                            }
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
            newList.add(list);
        }
        return newList;
    }

    private List<ContestantSubcategory> escapeConSubCategories(List<ContestantSubcategory> conSubcategory) {
        List<ContestantSubcategory> newList = new ArrayList<>();
        for (ContestantSubcategory list : conSubcategory) {
            list.setName(HtmlUtils.htmlEscape(list.getName(), "UTF-8"));
            newList.add(list);
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

//            if (race.getContestantCategoryId() != -1) {
//                List<ContestantSubcategory> contestantSubcategories;
//                contestantSubcategories = contestantSubcategoryService.getListByCategoryId(race.getContestantCategoryId());
//                model.addObject("con_categories", contestantSubcategories);
//            }
//            if (race.getTeamCategoryId() != -1) {
//                List<TeamSubcategory> teamSubcategories;
//                teamSubcategories = teamSubcategoryService.getListByCategoryId(race.getTeamCategoryId());
//                model.addObject("team_categories", teamSubcategories);
//            }

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
    public ModelAndView addSoloContestant(@RequestParam(value = "category", required = false) String category,
                                          @PathVariable("id") int race_id) {

        Contestant contestant;
        contestant = userToContestant(userController.getUser());
        contestant.setPaid(false);
        if (category == null) {
            contestant.setCategory("jaj");
        } else {
            contestant.setCategory(category);
        }

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