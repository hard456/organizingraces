package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.container.ContestantList;
import cz.zcu.fav.sportevents.form.SoloRegForm;
import cz.zcu.fav.sportevents.form.TeamRegForm;
import cz.zcu.fav.sportevents.model.*;
import cz.zcu.fav.sportevents.service.*;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
        ContestantList contestantList;

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
    public ModelAndView addSoloContestant(HttpServletRequest r, @Valid @ModelAttribute SoloRegForm soloRegForm,
                                          BindingResult bindingResult, @PathVariable("id") int race_id) {

        ModelAndView model = new ModelAndView();
        model.setViewName("race/race_reg_result");

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();

        model.addObject("race", race);
        model.addObject("user", user);

        if (bindingResult.hasErrors()) {
            model.addObject("invalid", true);
            model.addObject("result", "Something went wrong.");
            return model;
        }

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

        if(contestantService.getListByUserAndRaceId(user.getId(),race.getId()).size() != 0){
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

    @RequestMapping(value = "/race/{id}/addTeamByAdmin", method = RequestMethod.POST)
    public ModelAndView adminContestantsRegistration(@ModelAttribute("contestantList") ContestantList contestantList) {
        List<Contestant> contestants = contestantList.getContestants();
        ModelAndView model = new ModelAndView();
        model.setViewName("others/test");
        model.addObject("c", contestants);
        return model;
    }

    @RequestMapping(value = "/race/{id}/teamRegistration", method = RequestMethod.POST)
    public ModelAndView teamRegistration(HttpServletRequest r, @ModelAttribute("teamRegForm") TeamRegForm teamRegForm,
                                         BindingResult bindingResult, @PathVariable("id") int race_id) {

        ModelAndView model = new ModelAndView();
        model.setViewName("race/race_reg_result");

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();

        model.addObject("race", race);
        model.addObject("user", user);

        if (bindingResult.hasErrors()) {
            model.addObject("invalid", true);
            model.addObject("result", "Something went wrong.");
            return model;
        }

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

        if(contestantService.getListByUserAndRaceId(user.getId(),race.getId()).size() != 0){
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

}

