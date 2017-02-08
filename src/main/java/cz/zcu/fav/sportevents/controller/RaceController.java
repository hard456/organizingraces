package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.model.Race;
import cz.zcu.fav.sportevents.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class RaceController {

    @Autowired
    UserController userController;

    @Autowired
    RaceService raceService;

    @RequestMapping(value = "/create_race",method = RequestMethod.GET)
    public String createRace() {
        return "create_race";
    }

    @RequestMapping(value = "/my_races",method = RequestMethod.GET)
    public ModelAndView myRaces() {
        ModelAndView model = new ModelAndView();
        List<Race> list;
        list = raceService.listByUserId(userController.getUser().getId());
        model.setViewName("my_races");
        model.addObject("list",list);
        return model;
    }

    @RequestMapping(value = "/create_event",method = RequestMethod.POST)
    public ModelAndView createEvent(@RequestParam("name") String name) {

        ModelAndView model = new ModelAndView();
        model.setViewName("test");

        Race race = new Race();
        race.setName(name);
        race.setUserId(userController.getUser().getId());
        race.setEvaluation(false);

        model.addObject("name",race.getName());
        model.addObject("evaluation",race.isEvaluation());
        model.addObject("userid",race.getUserId());

        raceService.createRace(race);

        return model;
    }

    @RequestMapping(value = "/race/{id}", method = RequestMethod.GET)
    public ModelAndView race(@PathVariable("id") int race_id){
        ModelAndView model = new ModelAndView();
        model.setViewName("race");
        model.addObject("id",race_id);
        return model;
    }

}