package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.container.CreateTeamAjaxResponse;
import cz.zcu.fav.sportevents.form.CreateTeamForm;
import cz.zcu.fav.sportevents.model.*;
import cz.zcu.fav.sportevents.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SoloContestantController {

    @Autowired
    RaceService raceService;

    @Autowired
    UserService userService;

    @Autowired
    RaceCooperationService raceCooperationService;

    @Autowired
    ContestantService contestantService;

    @Autowired
    TeamSubcategoryService teamSubcategoryService;

    @Autowired
    TeamService teamService;

    /**
     * Zobrazení šablony závodníků bez týmů. Jen pro velikost týmu větší než jedna.
     * @param race_id
     * @return
     */
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
            if ((user = userService.getLoginUser()) != null) {
                if (raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
                    model.addObject("race_cooperator", true);
                } else {
                    model.addObject("race_cooperator", false);
                }
            }

            model.addObject("race", race);
            model.addObject("user", user);
            model.addObject("contestants", contestantService.getSoloContestants(race_id));
            if (race.getTeamCategory() != null) {
                model.addObject("team_categories", teamSubcategoryService.getListByCategoryId(race.getTeamCategory().getId()));
            }
            model.setViewName("race/contestants_solo");
            return model;
        }
    }

    /**
     * Pro smazání závodníka bez týmu.
     * @param r
     * @param contestantId ID závodníka
     * @param bindingResult
     * @param race_id
     * @return -1 - obecná chyba, ID závodníka - v pořádku
     */
    @RequestMapping(value = "/race/{id}/contestants/deleteSoloContestant", method = RequestMethod.POST)
    public
    @ResponseBody
    int deleteSoloContestant(HttpServletRequest r, @ModelAttribute("contestant") Integer contestantId,
                             BindingResult bindingResult, @PathVariable("id") int race_id) {

        User user = userService.getLoginUser();
        Race race = raceService.getRaceById(race_id);

        if (bindingResult.hasErrors()) {
            return -1;
        }

        if (user == null || race == null) {
            return -1;
        }

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return -1;
        }

        if (!r.getParameterMap().containsKey("contestant")) {
            return -1;
        }

        Contestant contestant = contestantService.getContestantById(contestantId);

        if (contestant == null) {
            return -1;
        }

        if (contestant.getTeam() != null) {
            return -1;
        }

        contestantService.delete(contestant);

        return contestantId;

    }

    /**
     * Vytvoření týmu ze závodníků bez týmu.
     * @param r rquest
     * @param createTeamForm kontejner s daty z formuláře
     * @param race_id
     * @return
     */
    @RequestMapping(value = "/race/{id}/createTeam", method = RequestMethod.POST)
    public
    @ResponseBody
    CreateTeamAjaxResponse createTeam(HttpServletRequest r, @RequestBody CreateTeamForm createTeamForm,
                                      @PathVariable("id") int race_id) {

        User user = userService.getLoginUser();
        Race race = raceService.getRaceById(race_id);
        Team team = new Team();
        List<Integer> newList;
        TeamSubcategory category;
        CreateTeamAjaxResponse response = new CreateTeamAjaxResponse();

        try {
            newList = createTeamForm.getContestants().stream().map(Integer::parseInt).collect(Collectors.toList());
        } catch (Exception e) {
            response.setValidation("Something went wrong.");
            return response;
        }

        if (user == null || race == null || createTeamForm.getTeamName() == null) {
            response.setValidation("Something went wrong.");
            return response;
        }

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            response.setValidation("You are not allowed to do that.");
            return response;
        }

        if (createTeamForm.getContestants() == null) {
            response.setValidation("List of solo contestants is empty.");
            return response;
        }

        if (race.getTeamCategory() != null) {
            try {
                category = teamSubcategoryService.getSubcategoryById(Integer.parseInt(createTeamForm.getTeamCategory()));
            } catch (Exception e) {
                response.setValidation("Something went wrong.");
                return response;
            }

            if (category == null || (category.getTeamCategory().getId() != race.getTeamCategory().getId())) {
                response.setValidation("Something went wrong.");
                return response;
            }
            team.setCategory(category);
        }

        if (createTeamForm.getTeamName().length() != 0) {
            createTeamForm.setTeamName(HtmlUtils.htmlEscape(createTeamForm.getTeamName(), "UTF-8"));
            if (teamService.getByRaceIdTeamName(race_id, createTeamForm.getTeamName()) != null) {
                response.setValidation("Team with this name already exists");
                return response;
            }
            if (createTeamForm.getTeamName().length() > 32 || createTeamForm.getTeamName().length() < 3) {
                response.setValidation("Team name (3 - 32 length)");
                return response;
            }
            team.setName(createTeamForm.getTeamName());
        }

        if (createTeamForm.getContestants().size() == 0) {
            response.setValidation("You didn't select a contestant.");
            return response;
        }

        List<Contestant> contestants = getContestantsByIds(newList);

        if (contestants == null) {
            response.setValidation("Something went wrong.");
            return response;
        }

        if (!validContestants(contestants)) {
            response.setValidation("Something went wrong.");
            return response;
        }
        team.setRace(race);
        teamService.save(team);
        assignTeamToContestants(contestants, team);

        response.setContestantId(newList);
        response.setValidation("ok");
        return response;
    }

    /**
     * Vrátí list závodníků z listu s indentifikačními číslý závodníků
     * @param ids list ID's závodníků
     * @return null - chyba, list závodníků - v pořádku
     */
    private List<Contestant> getContestantsByIds(List<Integer> ids) {
        List<Contestant> contestants = new ArrayList<>();
        for (Integer i : ids) {
            Contestant contestant = contestantService.getContestantById(i);
            if (contestant == null) {
                return null;
            }
            contestants.add(contestant);
        }
        return contestants;
    }

    /**
     * Validace listu závodníků, jestli už není závodník v jiném týmů.
     * @param contestants list závodníků
     * @return
     */
    private boolean validContestants(List<Contestant> contestants) {
        for (Contestant c : contestants) {
            if (c.getTeam() != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Přiřazení týmu k listu závodníků
     * @param contestants list závodníků
     * @param team tým
     */
    private void assignTeamToContestants(List<Contestant> contestants, Team team) {
        for (Contestant c : contestants) {
            c.setTeam(team);
            contestantService.update(c);
        }
    }

}
