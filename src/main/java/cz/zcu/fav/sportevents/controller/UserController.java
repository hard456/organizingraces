package cz.zcu.fav.sportevents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    @RequestMapping(value = "/loginform",method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/registration",method = RequestMethod.GET)
    public String registration(){
        return "registration";
    }

    @RequestMapping(value = "/account",method = RequestMethod.GET)
    public String account(){
        return "account";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String addStudent(ModelMap model) {
        model.addAttribute("name");
        model.addAttribute("age");
        model.addAttribute("id");

        return "result";
    }

}
