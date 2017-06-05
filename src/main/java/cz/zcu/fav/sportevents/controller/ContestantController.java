package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.form.UpdateContestantForm;
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
import java.util.List;

@Controller
public class ContestantController {

    @Autowired
    RaceService raceService;

    @Autowired
    UserService userService;

    @Autowired
    RaceCooperationService raceCooperationService;

    @Autowired
    ContestantService contestantService;

    @Autowired
    ContestantSubcategoryService contestantSubcategoryService;

    @Autowired
    TeamSubcategoryService teamSubcategoryService;

    @Autowired
    TeamService teamService;

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
            if ((user = userService.getLoginUser()) != null) {
                if (raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
                    model.addObject("race_cooperator", true);
                } else {
                    model.addObject("race_cooperator", false);
                }
            }

            model.addObject("contestants", contestantService.getContestantsByRaceId(race_id));
            model.addObject("race", race);
            model.addObject("user", user);
            if (race.getContestantCategory() != null) {
                model.addObject("categories", contestantSubcategoryService.getListByCategoryId(race.getContestantCategory().getId()));
            }

            model.setViewName("race/manage_contestants");
            return model;
        }
    }


    @RequestMapping(value = "/race/{id}/contestants", method = RequestMethod.GET)
    public ModelAndView showOnePersonTeam(@PathVariable("id") int race_id) {
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        if (race == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        } else {
            User user = userService.getLoginUser();
            model.addObject("race", race);
            model.addObject("user", user);
            if(race.getTeamCategory() != null){
                model.addObject("team_categories", teamSubcategoryService.getListByCategoryId(race.getTeamCategory().getId()));
            }
            if(race.getContestantCategory() != null){
                model.addObject("con_categories", contestantSubcategoryService.getListByCategoryId(race.getContestantCategory().getId()));
            }
            List<Contestant> contestants = contestantService.getContestantsByRaceId(race_id);
            model.addObject("contestants", contestants);
            if (user != null) {
                if (raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
                    model.addObject("race_cooperator", true);
                } else {
                    model.addObject("race_cooperator", false);
                }
            }
            model.setViewName("race/contestants");
            return model;
        }
    }

    @RequestMapping(value = "/race/{id}/contestants/changePaidValue", method = RequestMethod.POST)
    public
    @ResponseBody
    int changePaidStatus(HttpServletRequest r, @ModelAttribute("contestantId") Integer contestantId,
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

        Contestant contestant = contestantService.getContestantById(contestantId);

        if(contestant == null){
            return -1;
        }

        if(contestant.isPaid()){
            contestant.setPaid(false);
            contestantService.update(contestant);
            return 0;
        }
        else{
            contestant.setPaid(true);
            contestantService.update(contestant);
            return 1;
        }

    }

    @RequestMapping(value = "/race/{id}/contestants/updateContestant", method = RequestMethod.POST)
    public
    @ResponseBody
    String updateContestant(HttpServletRequest r, @ModelAttribute UpdateContestantForm updateContestantForm,
                         BindingResult bindingResult, @PathVariable("id") int race_id) {

        User user = userService.getLoginUser();
        Race race = raceService.getRaceById(race_id);


        if (bindingResult.hasErrors()) {
            return "something_went_wrong";
        }

        if (user == null || race == null) {
            return "something_went_wrong";
        }

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "something_went_wrong";
        }

        if(race.getContestantCategory() != null){
            if(!validUpdateContestantParameters(r, true)){
                return "something_went_wrong";
            }
        }
        else{
            if(!validUpdateContestantParameters(r, false)){
                return "something_went_wrong";
            }
        }

        Contestant contestant = contestantService.getContestantById(updateContestantForm.getConId());

        if(contestant == null || contestant.getRace().getId() != race.getId()){
            return "something_went_wrong";
        }

        Contestant newContestant = new Contestant();
        newContestant.setFirstname(HtmlUtils.htmlEscape(updateContestantForm.getContestant().getFirstname(),"UTF-8"));
        newContestant.setLastname(HtmlUtils.htmlEscape(updateContestantForm.getContestant().getLastname(),"UTF-8"));
        newContestant.setEmail(HtmlUtils.htmlEscape(updateContestantForm.getContestant().getEmail(),"UTF-8"));
        newContestant.setPhone(updateContestantForm.getContestant().getPhone().replaceAll("\\s+",""));
        newContestant.setTeam(contestant.getTeam());
        newContestant.setId(contestant.getId());
        newContestant.setRace(race);
        newContestant.setUser(contestant.getUser());
        newContestant.setPaid(contestant.isPaid());

        if(!validContestantData(newContestant)){
            return "data";
        }
        if(!newContestant.getPhone().isEmpty()){
            if (!newContestant.getPhone().matches("^(\\+420)? ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}$")) {
                return "phone";
            }
        }

        if(race.getContestantCategory() != null){
            ContestantSubcategory category = contestantSubcategoryService.getSubCategoryById(updateContestantForm.getConCategory());
            if(category == null || (category.getContestantCategory().getId() != race.getContestantCategory().getId())){
                return "something_went_wrong";
            }
            newContestant.setCategory(category);
        }
        if(race.getTeamCategory() != null && race.getTeamSize() == 1){
            if(updateContestantForm.getTeamCategory() == null){
                return "something_went_wrong";
            }
            TeamSubcategory teamSubcategory;
            teamSubcategory = teamSubcategoryService.getSubcategoryById(updateContestantForm.getTeamCategory());
            if(teamSubcategory == null){
                return "something_went_wrong";
            }
            TeamSubcategory teamSubcategory2;
            teamSubcategory2 = teamSubcategoryService.getSubcategoryByNameByCategoryId(teamSubcategory.getName(),race.getTeamCategory().getId());
            if(teamSubcategory2 == null || teamSubcategory.getId() != teamSubcategory2.getId()){
                return "something_went_wrong";
            }
            Team team = contestant.getTeam();
            team.setCategory(teamSubcategory);
            teamService.update(team);
        }
        contestantService.update(newContestant);
        return "ok";
    }

    private boolean validContestantData(Contestant contestant) {
        if(contestant.getFirstname().length() > 32 || contestant.getFirstname().length() < 3){
            return false;
        }
        if(contestant.getLastname().length() > 32 || contestant.getLastname().length() < 3){
            return false;
        }
        if(!contestant.getEmail().isEmpty()){
            if(contestant.getEmail().length() > 32 || contestant.getEmail().length() < 6){
                return false;
            }
            if(!EmailValidator.getInstance().isValid(contestant.getEmail())){
                return false;
            }
        }
        return true;
    }

    private boolean validUpdateContestantParameters(HttpServletRequest r, boolean conCategory) {
        if(!r.getParameterMap().containsKey("contestant.firstname")){
            return false;
        }
        if(!r.getParameterMap().containsKey("contestant.lastname")){
            return false;
        }
        if(!r.getParameterMap().containsKey("contestant.email")){
            return false;
        }
        if(!r.getParameterMap().containsKey("contestant.phone")){
            return false;
        }
        if(conCategory){
            if(!r.getParameterMap().containsKey("conCategory")){
                return false;
            }
        }
        if(!r.getParameterMap().containsKey("conId")){
            return false;
        }
        return true;
    }


}
