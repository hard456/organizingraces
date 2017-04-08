package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.form.CreateRaceForm;
import cz.zcu.fav.sportevents.model.*;
import cz.zcu.fav.sportevents.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CreateRaceController {

    @Autowired
    RaceService raceService;

    @Autowired
    TeamCategoryService teamCategoryService;

    @Autowired
    ContestantCategoryService contestantCategoryService;

    @Autowired
    TeamSubcategoryService teamSubcategoryService;

    @Autowired
    ContestantSubcategoryService contestantSubcategoryService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/create_race", method = RequestMethod.GET)
    public ModelAndView createRace() {
        ModelAndView model = new ModelAndView();
        model.setViewName("race/create_race");
        model.addObject("team_categories", teamCategoryService.getDefaultCategories());
        model.addObject("con_categories", contestantCategoryService.getDefaultCategories());
        return model;
    }

    @RequestMapping(value = "/create_event", method = RequestMethod.POST)
    public ModelAndView createEvent(HttpServletRequest request, @Valid @ModelAttribute("createRaceForm") CreateRaceForm createRaceForm, BindingResult result) {
        ModelAndView model = new ModelAndView();
        User user = userService.getLoginUser();
        if (user != null) {
            model.setViewName("race/race_create_result");
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
            race.setRegistration(true);

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

}
