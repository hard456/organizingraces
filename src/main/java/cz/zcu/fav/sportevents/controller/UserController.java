package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Zobrazení šablony osobních údajů uživatele.
     * @return
     */
    @RequestMapping(value = {"/userpage"}, method = RequestMethod.GET)
    public ModelAndView userPage() {
        ModelAndView model = new ModelAndView();
        model.addObject("user", userService.getLoginUser());
        model.setViewName("user/user");
        return model;
    }

}