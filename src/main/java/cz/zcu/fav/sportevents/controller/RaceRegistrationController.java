package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.form.AdminSoloRegForm;
import cz.zcu.fav.sportevents.form.AdminTeamRegForm;
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
import java.util.List;

@Controller
public class RaceRegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private RaceService raceService;

    @Autowired
    private ContestantService contestantService;

    @Autowired
    private RaceCooperationService raceCooperationService;

    @Autowired
    private TeamSubcategoryService teamSubcategoryService;

    @Autowired
    private ContestantSubcategoryService contestantSubcategoryService;

    @Autowired
    private TeamService teamService;

    @RequestMapping(value = "/race/{id}/registration", method = RequestMethod.GET)
    public ModelAndView race_registration(@PathVariable("id") int race_id) {
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

            model.setViewName("race/race_registration");
            return model;
        }

    }

    @RequestMapping(value = "/race/{id}/addSoloContestant", method = RequestMethod.POST)
    public @ResponseBody String addSoloContestant(HttpServletRequest r, @Valid @ModelAttribute SoloRegForm soloRegForm,
                                          BindingResult bindingResult, @PathVariable("id") int race_id) {


        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();

        if (bindingResult.hasErrors()) {
            return "something_went_wrong";
        }

        if (user == null || race == null) {
            return "something_went_wrong";
        }

        if(race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id,user.getId())) {
            if (contestantService.getListByUserAndRaceId(user.getId(), race.getId()).size() != 0) {
                return "registered_already";
            }
            if(!race.isRegistration()){
                return "registration_disabled";
            }
        }

        ContestantSubcategory conCategory = null;

        if(race.getContestantCategory() != null){
            List<ContestantSubcategory> subcategories;
            subcategories = contestantSubcategoryService.getListByCategoryId(race.getContestantCategory().getId());

            if (subcategories != null) {
                if (!r.getParameterMap().containsKey("category")) {
                    return "something_went_wrong";
                }
                conCategory = getConCategoryFromList(subcategories, soloRegForm.getCategory());
                if (conCategory == null) {
                    return "something_went_wrong";
                }
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

        return "ok";
    }

    @RequestMapping(value = "/race/{id}/teamRegistration", method = RequestMethod.POST)
    public @ResponseBody String teamRegistration(HttpServletRequest r, @ModelAttribute("teamRegForm") TeamRegForm teamRegForm,
                                         BindingResult bindingResult, @PathVariable("id") int race_id) {

        ModelAndView model = new ModelAndView();
        model.setViewName("race/race_reg_result");

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();

        model.addObject("race", race);
        model.addObject("user", user);

        if (bindingResult.hasErrors()) {
           return "something_went_wrong";
        }

        Contestant creator = new Contestant();
        Team team = new Team();

        if (user == null || race == null) {
            return "something_went_wrong";
        }

        if(race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id,user.getId())){
            if (contestantService.getListByUserAndRaceId(user.getId(), race.getId()).size() != 0) {
                return "registered_already";
            }
            if(!race.isRegistration()){
                model.addObject("invalid", true);
                model.addObject("result", "Registration disabled.");
                return "registration_disabled";
            }
        }

        if (race.getTeamCategory() != null) {
            if (!r.getParameterMap().containsKey("teamCategory")) {
                return "something_went_wrong";
            }
            List<TeamSubcategory> teamSubList;
            teamSubList = teamSubcategoryService.getListByCategoryId(race.getTeamCategory().getId());
            team.setCategory(getTeamCategoryFromList(teamSubList, teamRegForm.getTeamCategory()));
            if (team.getCategory() == null) {
                return "something_went_wrong";
            }
        }

        if (race.getContestantCategory() != null) {
            if (!r.getParameterMap().containsKey("conCategory")) {
                return "something_went_wrong";
            }
            List<ContestantSubcategory> conSubList;
            conSubList = contestantSubcategoryService.getListByCategoryId(race.getContestantCategory().getId());
            creator.setCategory(getConCategoryFromList(conSubList, teamRegForm.getConCategory()));
            if (creator.getCategory() == null) {
                return "something_went_wrong";
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
                return "something_went_wrong";
            }

            if (race.getContestantCategory() != null) {
                if (!validConListParameters(r, race.getTeamSize() - 1, true)) {
                    return "something_went_wrong";
                }
                contestants = validContestantList(teamRegForm.getContestants());
                if (contestants == null) {
                    return "values";
                }

                contestants = setCategoriesToConList(contestants, teamRegForm.getTeammateCategory(), race.getContestantCategory().getId());
                if (contestants == null) {
                    return "something_went_wrong";
                }
            } else {
                if (!validConListParameters(r, race.getTeamSize() - 1, false)) {
                    return "something_went_wrong";
                }
                contestants = validContestantList(teamRegForm.getContestants());
                if (contestants == null) {
                    return "values";
                }

            }
            if (teamRegForm.getTeamName().length() != 0) {
                team.setName(HtmlUtils.htmlEscape(teamRegForm.getTeamName(), "UTF-8"));
                if (team.getName().length() < 3 || team.getName().length() > 32) {
                    return "wrong_team_name";
                }
                if(teamService.getByRaceIdTeamName(race_id,team.getName()) != null){
                    model.addObject("invalid", true);
                    model.addObject("result", "Team with this name already exists.");
                    return "team_exists";
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

        return "ok";
    }

    private List<Contestant> validContestantList(List<Contestant> c) {

        for (int i = 0; i < c.size(); i++) {
            c.get(i).setFirstname(HtmlUtils.htmlEscape(c.get(i).getFirstname(), "UTF-8"));
            c.get(i).setLastname(HtmlUtils.htmlEscape(c.get(i).getLastname(), "UTF-8"));
            c.get(i).setEmail(HtmlUtils.htmlEscape(c.get(i).getEmail(), "UTF-8"));
            c.get(i).setPhone(HtmlUtils.htmlEscape(c.get(i).getPhone().replaceAll("\\s+",""), "UTF-8"));

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

    private List<Contestant> setPaidAttributeToConList(HttpServletRequest r, List<Contestant> c) {
        for (int i = 0; i < c.size(); i++) {
            if (r.getParameterMap().containsKey("contestants[" + i + "].paid")) {
                c.get(i).setPaid(true);
            }
        }
        return c;
    }

    @RequestMapping(value = "/race/{id}/adminSoloRegistration", method = RequestMethod.POST)
    public
    @ResponseBody
    String addContestantByAdmin(HttpServletRequest request, @ModelAttribute AdminSoloRegForm adminSoloRegForm, BindingResult bindingResult,
                                @PathVariable("id") int race_id) {

        if (bindingResult.hasErrors()) {
            return "fail";
        }

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();

        if (user == null || race == null) {
            return "fail";
        }

        if ((race.getUser().getId() != user.getId()) && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "fail";
        }

        Contestant contestant = new Contestant();
        contestant.setFirstname(HtmlUtils.htmlEscape(adminSoloRegForm.getContestant().getFirstname(), "UTF-8"));
        contestant.setLastname(HtmlUtils.htmlEscape(adminSoloRegForm.getContestant().getLastname(), "UTF-8"));
        contestant.setEmail(HtmlUtils.htmlEscape(adminSoloRegForm.getContestant().getEmail(), "UTF-8"));
        contestant.setPhone(adminSoloRegForm.getContestant().getPhone().replaceAll("\\s+",""));
        contestant.setRace(race);
        contestant.setUser(user);

        if (request.getParameterMap().containsKey("contestant.paid")) {
            contestant.setPaid(true);
        }

        if (race.getContestantCategory() != null) {
            if (!validAdminSoloRequestParameters(request, true)) {
                return "fail";
            }
            ContestantSubcategory category;
            category = contestantSubcategoryService.getSubcategoryByRaceIdByCategoryId(adminSoloRegForm.getCategory(), race.getContestantCategory().getId());
            if (category == null) {
                return "fail";
            }
            contestant.setCategory(category);
        } else {
            if (!validAdminSoloRequestParameters(request, false)) {
                return "fail";
            }
        }

        if (!validAdminSoloData(contestant)) {
            return "invalid";
        }

        contestantService.saveContestant(contestant);

        return "ok";
    }

    public boolean validAdminSoloRequestParameters(HttpServletRequest r, boolean contestantCategory) {
        if (!r.getParameterMap().containsKey("contestant.firstname")) {
            return false;
        }
        if (!r.getParameterMap().containsKey("contestant.lastname")) {
            return false;
        }
        if (!r.getParameterMap().containsKey("contestant.email")) {
            return false;
        }
        if (!r.getParameterMap().containsKey("contestant.phone")) {
            return false;
        }
        if (contestantCategory) {
            if (!r.getParameterMap().containsKey("category")) {
                return false;
            }
        }
        return true;
    }

    public boolean validAdminSoloData(Contestant contestant) {
        if (contestant.getLastname().length() > 32 || contestant.getLastname().length() < 3) {
            return false;
        }
        if (contestant.getFirstname().length() > 32 || contestant.getFirstname().length() < 3) {
            return false;
        }
        if (contestant.getEmail().length() > 0) {
            if (!EmailValidator.getInstance().isValid(contestant.getEmail()) || contestant.getEmail().length() > 32 || contestant.getEmail().length() < 6) {
                return false;
            }
        }
        if (contestant.getPhone().length() > 0) {
            if (!contestant.getPhone().matches("^(\\+420)? ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}$")) {
                return false;
            }
        }
        return true;
    }

    @RequestMapping(value = "/race/{id}/adminTeamRegistration", method = RequestMethod.POST)
    public
    @ResponseBody
    String addTeamByAdmin(HttpServletRequest request, @ModelAttribute("adminTeamRegForm") AdminTeamRegForm adminTeamRegForm,
                          BindingResult bindingResult, @PathVariable("id") int race_id) {

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();

        if (bindingResult.hasErrors()) {
            return "fail";
        }

        Team team = new Team();

        if (user == null) {
            return "fail";
        }

        if (race == null) {
            return "fail";
        }

        if (race.getTeamCategory() != null) {
            if (!request.getParameterMap().containsKey("teamCategory")) {
                return "fail";
            }
            List<TeamSubcategory> teamSubList;
            teamSubList = teamSubcategoryService.getListByCategoryId(race.getTeamCategory().getId());
            team.setCategory(getTeamCategoryFromList(teamSubList, adminTeamRegForm.getTeamCategory()));
            if (team.getCategory() == null) {
                return "fail";
            }
        }

        List<Contestant> contestants;

        if (race.getContestantCategory() != null) {
            if (!validConListParameters(request, adminTeamRegForm.getContestants().size(), true)) {
                return "fail";
            }
            contestants = validContestantList(adminTeamRegForm.getContestants());
            if (contestants == null) {
                return "invalid";
            }
            contestants = setCategoriesToConList(contestants, adminTeamRegForm.getTeammateCategory(), race.getContestantCategory().getId());
            if (contestants == null) {
                return "fail";
            }
        } else {
            if (!validConListParameters(request, adminTeamRegForm.getContestants().size(), false)) {
                return "fail";
            }
            contestants = validContestantList(adminTeamRegForm.getContestants());
            if (contestants == null) {
                return "invalid";
            }

        }
        if(race.getTeamSize() > 1){
            if(!request.getParameterMap().containsKey("teamName")){
                return "fail";
            }
            if (adminTeamRegForm.getTeamName().length() != 0) {
                team.setName(HtmlUtils.htmlEscape(adminTeamRegForm.getTeamName(), "UTF-8"));
                if(teamService.getByRaceIdTeamName(race_id,team.getName()) != null){
                    return "team_exists";
                }
                if (team.getName().length() < 3 || team.getName().length() > 32) {
                    return "team_name";
                }
            }
        }

        team.setRace(race);
        teamService.save(team);

        contestants = setPaidAttributeToConList(request, contestants);

        for (Contestant c : contestants) {
            c.setRace(race);
            c.setUser(user);
            c.setTeam(team);
            contestantService.saveContestant(c);
        }

        return "ok";
    }

}

