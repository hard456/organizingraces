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
import org.springframework.web.bind.annotation.ResponseBody;
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

    /**
     * Zobrazení šablony pro vytvoření závodu.
     * @return
     */
    @RequestMapping(value = "/create_race", method = RequestMethod.GET)
    public ModelAndView createRace() {
        ModelAndView model = new ModelAndView();
        model.setViewName("race/create_race");
        model.addObject("team_categories", teamCategoryService.getDefaultCategories());
        model.addObject("con_categories", contestantCategoryService.getDefaultCategories());
        return model;
    }

    /**
     * Metoda pro zpracování dat pro vytvoření závodu.
     * @param request
     * @param createRaceForm kontejner s daty z formuláře
     * @param result
     * @return "values" - špatně hodnoty, "something_went_wrong" - obecná chyba,
     * "race_name_exists" - závod s tímto názvem existuje, ID závodu - proběhlo v pořádku
     */
    @RequestMapping(value = "/create_event", method = RequestMethod.POST)
    public @ResponseBody String createEvent(HttpServletRequest request, @Valid @ModelAttribute("createRaceForm") CreateRaceForm createRaceForm, BindingResult result) {
        User user = userService.getLoginUser();
        if (user != null) {
            if (result.hasErrors()) {
                return "values";
            }
            if (!validCreateRaceParameters(request)) {
                return "values";
            }
            Race race = new Race();
            race.setName(HtmlUtils.htmlEscape(createRaceForm.getRace().getName(), "UTF-8"));
            race.setTeamSize(createRaceForm.getRace().getTeamSize());
            race.setUser(user);
            race.setRegistration(true);

            if (race.getName().length() > 32 || race.getName().length() < 3) {
                return "values";
            }

            if (!raceService.isExistRaceByName(race.getName())) {
                if (validCreateEventData(createRaceForm)) {

                    if (createRaceForm.getTeamRadio().equals("own")) {
                        if (escapeTeamSubCategories(createRaceForm.getTeamSubCategories()) == null) {
                            return "values";
                        }
                    }
                    if (createRaceForm.getConRadio().equals("own")) {
                        if (escapeConSubCategories(createRaceForm.getContestantSubCategories()) == null) {
                            return "values";
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
                    return Integer.toString(race.getId());

                } else {
                    return "values";
                }

            } else {
                return "race_name_exists";
            }

        } else {
            return "something_went_wrong";
        }
    }

    /**
     * Pro validaci dat z formuláře pro vytvoření závodu.
     * @param createRaceForm kontejner s daty pro validaci
     * @return false - chyba, true - v pořádku
     */
    private boolean validCreateEventData(CreateRaceForm createRaceForm) {
        if (createRaceForm.getTeamRadio().equals("defaultValue")) {
            if (createRaceForm.getDefTeamCategoryId() == null) {
                return false;
            }
            if (!teamCategoryService.exists(createRaceForm.getDefTeamCategoryId())) {
                return false;
            }
        }
        if (createRaceForm.getConRadio().equals("defaultValue")) {
            if (createRaceForm.getDefConCategoryId() == null) {
                return false;
            }
            if (!contestantCategoryService.exists(createRaceForm.getDefConCategoryId())) {
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

    /**
     * Kontrola existence všech parametrů požadavku na vytvoření závodu.
     * @param request
     * @return false - chyba, true - v pořádku
     */
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

    /**
     * Přiřazení kategorie závodníka listu podkategorii závodníka
     * @param conSubcategory list podkategorií závodníka
     * @param id ID kategorie závodníka
     * @return list podkategorií závodníka
     */
    private List<ContestantSubcategory> setConCategoryIdToList(List<ContestantSubcategory> conSubcategory, int id) {
        List<ContestantSubcategory> newList = new ArrayList<>();
        for (ContestantSubcategory list : conSubcategory) {
            list.setContestantCategory(contestantCategoryService.getCategoryById(id));
            newList.add(list);
        }
        return newList;
    }

    /**
     * Přiřazení kategorie týmu listu podkategorií týmu
     * @param teamSubcategory list podkategorií týmu
     * @param id ID kategorie týmu
     * @return list podkategorií týmu
     */
    private List<TeamSubcategory> setTeamCategoryIdToList(List<TeamSubcategory> teamSubcategory, int id) {
        List<TeamSubcategory> newList = new ArrayList<>();
        for (TeamSubcategory list : teamSubcategory) {
            list.setTeamCategory(teamCategoryService.getCategoryById(id));
            newList.add(list);
        }
        return newList;
    }

    /**
     * Pro vyescapování listu týmových podkategorií s ověřením požadované délky
     * @param teamSubcategory list podkategorií
     * @return list podkategorií, null - při chybě
     */
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

    /**
     * Vyescapování listu podkategorií závodníka s ověřením požadované délky
     * @param conSubcategory list podkategorií
     * @return list podkategorií, null - při chybě
     */
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
