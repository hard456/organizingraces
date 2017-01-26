package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.model.User;
import cz.zcu.fav.sportevents.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

        @Autowired
        private RegistrationService regService;

        @RequestMapping(value = "/",method = RequestMethod.GET)
        public String home() {
        return "home";
    }

        @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
        public ModelAndView loginPage() {
            ModelAndView model = new ModelAndView();
            model.setViewName("login");
            return model;
        }

        @RequestMapping(value = {"/registration"}, method = RequestMethod.GET)
        public ModelAndView registrationPage() {
            ModelAndView model = new ModelAndView();
            model.setViewName("registration");
            return model;
        }

        @RequestMapping(value = {"/accessdenied"}, method = RequestMethod.GET)
        public ModelAndView accessDeniedPage() {
            ModelAndView model = new ModelAndView();
            model.setViewName("accessdenied");
            return model;
        }

        @RequestMapping(value = {"/userpage"}, method = RequestMethod.GET)
        public ModelAndView userPage() {
            ModelAndView model = new ModelAndView();
            model.addObject("user", getUser());
            model.setViewName("user");
            return model;
        }

        @RequestMapping(value = "/logout", method = RequestMethod.GET)
        public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            return "redirect:/";
        }

        private String getUser() {
            String userName = null;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                userName = ((UserDetails) principal).getUsername();
            } else {
                userName = principal.toString();
            }
            return userName;
        }

        @RequestMapping(value = {"/addUser"}, method = RequestMethod.POST)
        public ModelAndView addUser(@RequestParam(value="passwordAgain") String passwordAgain, User user) {
            ModelAndView model = new ModelAndView();
            model.setViewName("reg_result");
            if(!regService.checkSizeParameters(user)){
                model.addObject("message", "Wrong input size: Password(8-256), Login(3-32), Firstname(2-32), Lastname(2-32), email(6-32)");
                return model;
            }
            else if (!regService.checkUserName(user)) {
                model.addObject("message", "Name already used.");
                return model;
            } else if (!regService.checkEmail(user)) {
                model.addObject("message", "Email already used.");
                return model;
            } else if (!user.getPassword().equals(passwordAgain)) {
                model.addObject("message", "Passwords are not identical.");
                return model;
            } else {
                regService.addUser(user, passwordAgain);
                model.addObject("message", "Registration OK");
                return model;
            }
        }

}